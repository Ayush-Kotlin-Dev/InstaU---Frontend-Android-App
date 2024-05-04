package ayush.ggv.instau.presentation.screens.post

import androidx.compose.runtime.Composable
import ayush.ggv.instau.common.fakedata.Post
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
@Destination
fun PostDetail(
    navigator: DestinationsNavigator,
    postId: String
) {
    val viewModel: PostDetailScreenViewModel = koinViewModel()

    PostDetailScreen(
        postUiState = viewModel.postUiState,
        commentsUiState = viewModel.commentsUiState,
        onCommentMoreIconClick = {},

        onProfileClick = {},
        onAddCommentClick = {},
        fetchData = {
            viewModel.fetchData(postId)

        }
    )


}