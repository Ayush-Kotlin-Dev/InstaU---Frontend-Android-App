package ayush.ggv.instau.presentation.screens.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ayush.ggv.instau.R
import ayush.ggv.instau.presentation.components.CommentListItem
import ayush.ggv.instau.presentation.components.PostListItem
import ayush.ggv.instau.common.fakedata.Comment
import ayush.ggv.instau.common.fakedata.sampleComments
import ayush.ggv.instau.common.fakedata.samplePosts
import ayush.ggv.instau.ui.theme.LargeSpacing
import ayush.ggv.instau.ui.theme.SocialAppTheme
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun PostDetailScreen(
    modifier: Modifier = Modifier,
    postUiState: PostDetailUiState,
    commentsUiState: CommentsUiState,
    onCommentMoreIconClick: (Comment) -> Unit,
    onProfileClick: (Long) -> Unit,
    onAddCommentClick: () -> Unit,
    fetchData: () -> Unit
) {

    if (postUiState.isLoading && commentsUiState.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (postUiState.post != null) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.surface)
        ) {
            item(key = "post_item") {
                PostListItem(
                    post = postUiState.post,
                    onPostClick = { /*TODO*/ },
                    onProfileClick = onProfileClick,
                    onLikeClick = { /*TODO*/ },
                    onCommentClick = { /*TODO*/ },
                    isDetailScreen = true
                )
            }
            item(key = "comments_header_section") {
                CommentSectionHeader(
                    onAddCommentsClick = onAddCommentClick
                )
            }
            items(
                items = sampleComments,
                key = { comment -> comment.id }
            ) {
                Divider()
                CommentListItem(
                    comment = it,
                    onProfileClick = onProfileClick
                ) {
                    onCommentMoreIconClick(it)
                }
            }


        }

    } else {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(LargeSpacing)
            ) {
                Text(
                    text = stringResource(id = R.string.loading_error_message),
                    style = MaterialTheme.typography.h6
                )
                OutlinedButton(onClick = { fetchData() }) {
                    Text(text = stringResource(id = R.string.retry_button_text))
                }

            }
        }

    }

    LaunchedEffect(key1 = Unit, block = {
        fetchData()
    })

}

@Composable
fun CommentSectionHeader(
    modifier: Modifier = Modifier,
    onAddCommentsClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(LargeSpacing),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.comments_label),
            style = MaterialTheme.typography.subtitle1
        )
        OutlinedButton(onClick = { onAddCommentsClick() }) {
            Text(text = stringResource(id = R.string.add_comment_button_label))
        }
    }
}

//@Preview
//@Composable
//fun PreviewPostDetailScreen() {
//    SocialAppTheme {
//        Surface {
//            PostDetailScreen(
//                postUiState = PostDetailUiState(
//                    isLoading = false,
//                    post = samplePosts.first()
//                ),
//                commentsUiState = CommentsUiState(
//                    isLoading = false,
//                    comments = sampleComments
//                ),
//                onCommentMoreIconClick = {},
//                onProfileClick = {},
//                onAddCommentClick = {},
//                fetchData = {}
//            )
//        }
//    }
//}