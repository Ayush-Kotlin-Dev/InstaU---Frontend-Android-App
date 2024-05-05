package ayush.ggv.instau.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.common.datastore.UserSettings
import ayush.ggv.instau.common.datastore.toAuthResultData
import ayush.ggv.instau.common.fakedata.Post
import ayush.ggv.instau.common.fakedata.samplePosts
import ayush.ggv.instau.common.fakedata.sampleUsers
import ayush.ggv.instau.domain.usecases.postusecase.PostUseCase
import ayush.ggv.instau.domain.usecases.signinusecase.SignInuseCase
import ayush.ggv.instau.presentation.screens.home.onboarding.OnBoardingUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ayush.ggv.instau.util.Result

class HomeScreenViewModel(
    private val postUseCase: PostUseCase,
    private val dataStore: DataStore<UserSettings>
) : ViewModel(

) {
    val token = mutableStateOf("")
    val  currentUserId = mutableStateOf(-1L)

    var postsUiState by mutableStateOf(PostsUiState())
        private set

    var onBoardingUiState by mutableStateOf(OnBoardingUiState())
        private set

    init {
        fetchData()
    }

    //fetchPosts()
    fun fetchData() {
        onBoardingUiState = onBoardingUiState.copy(isLoading = true)
        postsUiState = postsUiState.copy(isLoading = true)
        val page = 1
        val limit = 10
        viewModelScope.launch {
            dataStore.data.map { it.toAuthResultData() }.collect { userSettings ->
                currentUserId.value = userSettings.id
                token.value = userSettings.token
            }
        }

        viewModelScope.launch {
            delay(1000)

            // Fetch posts
            val result = postUseCase(currentUserId.value, page , limit, token.value)

            when (result) {
                is Result.Success -> {
                    val convertedPosts = result.data?.posts?.map { modelPost ->
                        Post(
                            id = modelPost.postId.toString(),
                            text = modelPost.caption,
                            imageUrl = modelPost.imageUrl,
                            createdAt = modelPost.createdAt,
                            likesCount = modelPost.likesCount,
                            commentCount = modelPost.commentsCount,
                            authorId = modelPost.userId.toInt(),
                            authorName = modelPost.userName,
                            authorImage = modelPost.userImageUrl?:"",
                            isLiked = modelPost.isLiked,
                            isOwnPost = modelPost.isOwnPost
                        )
                    }
                    postsUiState = postsUiState.copy(
                        posts = convertedPosts!!.reversed(),
                        isLoading = false
                    )
                }

                is Result.Error -> {
                    postsUiState = postsUiState.copy(
                        error = result.message,
                        isLoading = false
                    )
                }

                is Result.Loading -> {
                    postsUiState = postsUiState.copy(isLoading = true)
                }
            }

            onBoardingUiState = onBoardingUiState.copy(
                users = sampleUsers,
                isLoading = false,
                shouldShowOnBoarding = true
            )
        }

    }
}


data class PostsUiState(
    val isLoading: Boolean = false,
    val posts: List<Post> = listOf(),
    val error: String? = null
)