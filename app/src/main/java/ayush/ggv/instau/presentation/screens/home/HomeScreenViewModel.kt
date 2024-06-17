package ayush.ggv.instau.presentation.screens.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadType
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.room.withTransaction
import ayush.ggv.instau.common.datastore.UserSettings
import ayush.ggv.instau.common.datastore.toAuthResultData
import ayush.ggv.instau.dao.post.PostsDatabase
import ayush.ggv.instau.data.posts.domain.repository.PostRepository
import ayush.ggv.instau.domain.usecases.followsusecase.SuggestionsUseCase
import ayush.ggv.instau.domain.usecases.postsusecase.GetPostsStreamUseCase
import ayush.ggv.instau.model.Post
import ayush.ggv.instau.presentation.screens.home.onboarding.OnBoardingUiState
import ayush.ggv.instau.util.ResponseResource
import ayush.ggv.instau.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val dataStore: DataStore<UserSettings>,
    private val suggestionsUseCase: SuggestionsUseCase,
    private val postsStreamUseCase: GetPostsStreamUseCase,
    private val repository: PostRepository,
) : ViewModel(

) {

    val token = mutableStateOf("")
    val currentUserId = mutableStateOf(-1L)

    var postsUiState by mutableStateOf(PostsUiState())
        private set


    var onBoardingUiState by mutableStateOf(OnBoardingUiState())
        private set
    var newPostsAvailable by mutableStateOf(false)

    init {
        fetchData()
    }

    fun getPosts(userId: Long, token: String): Flow<PagingData<Post>> {
        val newResult =
            postsStreamUseCase(userId, token).flowOn(Dispatchers.IO).cachedIn(viewModelScope)
        return newResult
    }

    fun connectToSocket() {
        viewModelScope.launch {
            Log.d("HomeScreen", "connectToSocket: ")
            val result = repository.connectToSocket(currentUserId.value, token.value)
            Log.d("HomeScreen", "connectToSocket: $result")
            when (result) {
                is ResponseResource.Error -> {
                    onBoardingUiState = onBoardingUiState.copy(
                        error = result.errorMessage
                    )
                    Log.d("HomeScreen", "connectToSocket: ${result.errorMessage}")

                }
                //TODO i will also get details of the post (DELETED OR ADDED) from the server ,
                // and directly manipulate the database with that post details rather than deleting the whole database

                is ResponseResource.Success -> {
                    repository.receiveMessage().onEach {
                        Log.d("HomeScreen", "connectToSocket: $it")
                        if (it == "added" || it == "deleted") {
                            //how can i refresh the posts here ?
                            newPostsAvailable = true
                        }
                    }.flowOn(Dispatchers.IO).launchIn(viewModelScope)
                }
            }
        }
    }

    fun fetchData() {
        onBoardingUiState = onBoardingUiState.copy(isLoading = true)
        postsUiState = postsUiState.copy(isLoading = true)

        viewModelScope.launch {
            dataStore.data.map { it.toAuthResultData() }.collect { userSettings ->
                currentUserId.value = userSettings.id
                token.value = userSettings.token
            }
        }

        viewModelScope.launch {
            delay(500)

            // Fetch posts
            val newResult = getPosts(userId = currentUserId.value, token = token.value)
            postsUiState = postsUiState.copy(
                currentPostResult = newResult,
                isLoading = false
            )


            val resultSuggestions = suggestionsUseCase(currentUserId.value, token.value)
            when (resultSuggestions) {
                is Result.Success -> {
                    onBoardingUiState = onBoardingUiState.copy(
                        users = resultSuggestions.data?.follows ?: listOf(),
                        isLoading = false,
                        shouldShowOnBoarding = resultSuggestions.data?.follows?.isNotEmpty()
                            ?: false
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

    fun disconnectSocket() {
        viewModelScope.launch { repository.disconnectSocket() }
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