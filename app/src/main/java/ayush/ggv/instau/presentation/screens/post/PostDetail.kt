package ayush.ggv.instau.presentation.screens.post

import androidx.compose.runtime.Composable
import ayush.ggv.instau.model.NewCommentParams
import ayush.ggv.instau.model.RemoveCommentParams
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
@Destination
fun PostDetail(
    navigator: DestinationsNavigator,
    postId: Long,
) {
    val viewModel: PostDetailScreenViewModel = koinViewModel()

    PostDetailScreen(
        postUiState = viewModel.postUiState,
        commentsUiState = viewModel.commentsUiState,
        onProfileClick = {},
        onAddCommentClick = {
            viewModel.addComment(
                newCommentParams = NewCommentParams(
                    postId = postId,
                    content = it
                )
            )
        },
        fetchData = {
            viewModel.fetchData(postId)
        },
        onDeleteClick = {
            viewModel.deleteComment(
                RemoveCommentParams(
                    postId = postId,
                    commentId = it
                )
            )
        }
    )


}