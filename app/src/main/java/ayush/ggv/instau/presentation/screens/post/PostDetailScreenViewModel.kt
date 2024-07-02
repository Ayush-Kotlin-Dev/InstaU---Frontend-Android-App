package ayush.ggv.instau.presentation.screens.post

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.domain.usecases.postcommentusecase.CommentUseCase
import ayush.ggv.instau.domain.usecases.postcommentusecase.DeleteCommentUseCase
import ayush.ggv.instau.domain.usecases.postcommentusecase.GetCommentsUseCase
import ayush.ggv.instau.domain.usecases.postsusecase.GetPostByIdUseCase
import ayush.ggv.instau.model.NewCommentParams
import ayush.ggv.instau.model.Post
import ayush.ggv.instau.model.PostComment
import ayush.ggv.instau.model.RemoveCommentParams
import ayush.ggv.instau.util.Result
import kotlinx.coroutines.launch

class PostDetailScreenViewModel(
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val commentUseCase: CommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val getCommentsUseCase: GetCommentsUseCase

) :ViewModel() {

    var postUiState by mutableStateOf(PostDetailUiState())

    var commentsUiState by mutableStateOf(CommentsUiState())

    fun fetchData(postId : Long ){
        viewModelScope.launch {
            postUiState = postUiState.copy(isLoading = true)
            commentsUiState = commentsUiState.copy(isLoading = true)


            val postResult = getPostByIdUseCase(postId)
            val comment = getCommentsUseCase(postId, 1 , 10)

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

            commentsUiState = when(comment){
                is Result.Success -> {
                    commentsUiState.copy(
                        isLoading = false,
                        comments = comment.data?.comments ?: listOf( )
                    )
                }

                is Result.Error -> {
                    commentsUiState.copy(
                        isLoading = false,
                        errorMessage = comment.message
                    )
                }

                is Result.Loading -> TODO()
            }

        }

    }

    fun addComment(newCommentParams: NewCommentParams){
        viewModelScope.launch {
            val result = commentUseCase(newCommentParams)
            when(result){
                is Result.Success -> {
                    val newComment = result.data?.comment
                    commentsUiState = commentsUiState.copy(
                        comments = commentsUiState.comments + newComment!!
                    )
                }
                is Result.Error -> {
                    // Show error message
                }
                is Result.Loading -> {
                    // Show loading
                }
            }
        }
    }

    fun deleteComment(removeCommentParams: RemoveCommentParams){
        viewModelScope.launch {
            val result = deleteCommentUseCase(removeCommentParams)
            when(result){
                is Result.Success -> {
                    val deletedComment = result.data?.comment
                    commentsUiState = commentsUiState.copy(
                        comments = commentsUiState.comments.filter { it.commentId != deletedComment?.commentId }
                    )
                }
                is Result.Error -> {
                    // Show error message
                }
                is Result.Loading -> {
                    // Show loading
                }
            }
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
    val comments : List<PostComment> = listOf(),
    val errorMessage : String? = null
)