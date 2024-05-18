package ayush.ggv.instau.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.common.datastore.UserSettings
import ayush.ggv.instau.common.datastore.toAuthResultData
import ayush.ggv.instau.domain.usecases.followsusecase.SuggestionsUseCase
import ayush.ggv.instau.domain.usecases.postusecase.PostUseCase
import ayush.ggv.instau.model.Post
import ayush.ggv.instau.presentation.screens.home.onboarding.OnBoardingUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ayush.ggv.instau.util.Result

class HomeScreenViewModel(
    private val postUseCase: PostUseCase,
    private val dataStore: DataStore<UserSettings>,
    private val suggestionsUseCase: SuggestionsUseCase
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
            delay(500)

            // Fetch posts
            val result = postUseCase(currentUserId.value, page , limit, token.value)
            val resultSuggestions = suggestionsUseCase(currentUserId.value, token.value)


            when (result) {
                is Result.Success -> {
                    val convertedPosts = result.data?.posts?.map { modelPost ->
                        Post(
                            postId = modelPost.postId,
                            caption = modelPost.caption,
                            imageUrl = modelPost.imageUrl,
                            createdAt = modelPost.createdAt,
                            likesCount = modelPost.likesCount,
                            commentsCount = modelPost.commentsCount,
                            userId = modelPost.userId,
                            userName = modelPost.userName,
                            userImageUrl = modelPost.userImageUrl ?: "",
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
            when (resultSuggestions) {
                is Result.Success -> {
                    onBoardingUiState = onBoardingUiState.copy(
                        users = resultSuggestions.data?.follows ?: listOf(),
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
}


data class PostsUiState(
    val isLoading: Boolean = false,
    val posts: List<Post> = listOf(),
    val error: String? = null
)