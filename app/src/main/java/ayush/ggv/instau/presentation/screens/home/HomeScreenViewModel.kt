package ayush.ggv.instau.presentation.screens.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import ayush.ggv.instau.common.datastore.UserSettings
import ayush.ggv.instau.data.onboarding.domain.OnboardingRepository
import ayush.ggv.instau.data.posts.domain.repository.PostRepository
import ayush.ggv.instau.domain.usecases.followsusecase.SuggestionsUseCase
import ayush.ggv.instau.domain.usecases.postlikeusecase.PostLikeUseCase
import ayush.ggv.instau.domain.usecases.postlikeusecase.PostUnLikeUseCase
import ayush.ggv.instau.domain.usecases.postsusecase.DeletePostUseCase
import ayush.ggv.instau.domain.usecases.postsusecase.GetPostsStreamUseCase
import ayush.ggv.instau.model.Post
import ayush.ggv.instau.presentation.screens.home.onboarding.OnBoardingUiState
import ayush.ggv.instau.util.ResponseResource
import ayush.ggv.instau.util.Result
import com.google.firebase.storage.FirebaseStorage
import instaU.ayush.com.model.LikeParams
import instaU.ayush.com.model.LikeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val suggestionsUseCase: SuggestionsUseCase,
    private val postsStreamUseCase: GetPostsStreamUseCase,
    private val repository: PostRepository,
    private val onboardingRepository: OnboardingRepository,
    private val deletePostUseCase: DeletePostUseCase,
    private val postLikeUseCase: PostLikeUseCase,
    private val PostUnLikeUseCase: PostUnLikeUseCase
) : ViewModel() {

    var postsUiState by mutableStateOf(PostsUiState())
        private set
    var onBoardingUiState by mutableStateOf(OnBoardingUiState())
        private set


    val firebaseStorage = FirebaseStorage.getInstance()
    val likeResult = MutableLiveData<Result<LikeResponse>>()
    val isPostLiked = MutableLiveData<Boolean>()
    val likesCount = MutableLiveData<Int>()


    private val _onBoardingCompleted = MutableStateFlow<Boolean?>(null)
    val onBoardingCompleted: StateFlow<Boolean?> = _onBoardingCompleted

    var newPostsAvailable by mutableStateOf(false)

    init {
        initializeOnBoardingState()
        observeOnboardingState()
    }

    private fun initializeOnBoardingState() {
        viewModelScope.launch(Dispatchers.IO) {
            onboardingRepository.getOnBoardingState()
                .collect { state ->
                    _onBoardingCompleted.value = state
                }
        }
    }

    private fun observeOnboardingState() {
        viewModelScope.launch {
            onBoardingCompleted.filterNotNull().collect { completed ->
                fetchInitialData(completed)
            }
        }
    }

    private fun fetchInitialData(completed: Boolean) {
        if (!completed) {
            fetchOnboardingSuggestions()
        }
        fetchData()
    }

    fun getPosts(): Flow<PagingData<Post>> {
        return postsStreamUseCase().flowOn(Dispatchers.IO).cachedIn(viewModelScope)
    }

    fun connectToSocket() {
        viewModelScope.launch {
            when (val result = repository.connectToSocket()) {
                is ResponseResource.Error -> {
                    onBoardingUiState = onBoardingUiState.copy(
                        error = result.errorMessage
                    )
                }
                is ResponseResource.Success -> {
                    repository.receiveMessage().onEach {
                        if (it == "added" || it == "deleted") {
                            newPostsAvailable = true
                        }
                    }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
                }
            }
        }
    }

    fun fetchData() {
        viewModelScope.launch {
            postsUiState = postsUiState.copy(isLoading = true)
            delay(500)
            getPosts().collect { pagingData ->
                postsUiState = postsUiState.copy(
                    currentPostResult = flowOf(pagingData),
                    isLoading = false
                )
            }
        }
    }

    private fun fetchOnboardingSuggestions() {
        viewModelScope.launch {
            when (val resultSuggestions = suggestionsUseCase()) {
                is Result.Success -> {
                    onBoardingUiState = onBoardingUiState.copy(
                        users = resultSuggestions.data?.follows.orEmpty(),
                        isLoading = false,
                        shouldShowOnBoarding = resultSuggestions.data?.follows?.isNotEmpty() ?: false
                    )
                }
                is Result.Error -> {
                    onBoardingUiState = onBoardingUiState.copy(
                        error = resultSuggestions.message,
                        isLoading = false
                    )
                }
                is Result.Loading -> {
                    onBoardingUiState = onBoardingUiState.copy(isLoading = true)
                }
            }
        }
    }
    fun deletePost(postId: Long, imageUrl: String) {
        viewModelScope.launch {
            deletePostUseCase(postId)
            // Delete the image from Firebase Storage
            val imageRef = firebaseStorage.getReferenceFromUrl(imageUrl)
            imageRef.delete().addOnSuccessListener {
                // Image deleted successfully
            }.addOnFailureListener {
                // Failed to delete the image
            }
        }
    }
    fun likePost(likeParams: LikeParams) {
        viewModelScope.launch {
            val result = postLikeUseCase(likeParams)
            likeResult.value = result
            if (result is Result.Success) {
                isPostLiked.value = result.data?.success
                if (result.data?.success == true) {
                    likesCount.value = (likesCount.value ?: 0) + 1
                } else {
                    likesCount.value = (likesCount.value ?: 0) - 1
                }
            }
        }
    }

    fun saveOnBoardingState(completed: Boolean) {
        viewModelScope.launch {
            onboardingRepository.saveOnBoardingState(completed)
        }
    }

    fun disconnectSocket() {
        viewModelScope.launch {
            repository.disconnectSocket()
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnectSocket()
    }
}

data class PostsUiState(
    val isLoading: Boolean = false,
    var currentPostResult: Flow<PagingData<Post>>? = null,
    val error: String? = null
)
