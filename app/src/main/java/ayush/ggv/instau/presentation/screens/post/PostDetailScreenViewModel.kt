package ayush.ggv.instau.presentation.screens.post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.common.fakedata.Comment
import ayush.ggv.instau.common.fakedata.sampleComments
import ayush.ggv.instau.common.fakedata.samplePosts
import ayush.ggv.instau.domain.usecases.postsusecase.GetPostByIdUseCase
import ayush.ggv.instau.model.Post
import ayush.ggv.instau.util.Result
import kotlinx.coroutines.launch

class PostDetailScreenViewModel(
    private val getUserByIdUseCase: GetPostByIdUseCase
) :ViewModel() {

    var postUiState by mutableStateOf(PostDetailUiState())

    var commentsUiState by mutableStateOf(CommentsUiState())

    fun fetchData(postId : Long , currentUserId : Long , token : String){
        viewModelScope.launch {
            postUiState = postUiState.copy(isLoading = true)
            commentsUiState = commentsUiState.copy(isLoading = true)

            val postResult = getUserByIdUseCase(postId, currentUserId, token)
            when(postResult){
                is Result.Success -> {
                    postUiState = postUiState.copy(
                        isLoading = false,
                        post = postResult.data?.post
                    )
                }
                is Result.Error -> {
                    postUiState = postUiState.copy(
                        isLoading = false,
                        errorMessage = postResult.message
                    )
                }

                is Result.Loading -> TODO()
            }

            commentsUiState = commentsUiState.copy(
                isLoading = false,
                comments = sampleComments
            )
        }

    }

}

data class PostDetailUiState(
    val isLoading : Boolean = false,
    val post : Post? = null,
    val errorMessage : String? = null
)

data class CommentsUiState(
    val isLoading : Boolean = false,
    val comments : List<Comment> = listOf(),
    val errorMessage : String? = null
)