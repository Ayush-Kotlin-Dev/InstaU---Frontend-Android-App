package ayush.ggv.instau.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ayush.ggv.instau.common.components.PostListItem
import ayush.ggv.instau.common.fakedata.FollowsUser
import ayush.ggv.instau.common.fakedata.Post
import ayush.ggv.instau.common.fakedata.samplePosts
import ayush.ggv.instau.common.fakedata.sampleUsers
import ayush.ggv.instau.home.onboarding.OnBoardingSection
import ayush.ggv.instau.home.onboarding.OnBoardingUiState
import com.ramcosta.composedestinations.annotation.Destination


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onBoardingUiState: OnBoardingUiState,
    postsUiState: PostsUiState,
    onPostClick : (Post) -> Unit,
    onProfileClick : (Int) -> Unit,
    onLikeClick : (String) -> Unit,
    onCommentClick : (String) -> Unit,

    //onboarding
    onBoardingFinish : () -> Unit,
    onUserClick : (FollowsUser) -> Unit,
    onFollowClick : (Boolean ,FollowsUser) -> Unit,

    fetchData : () -> Unit
    ) {
    val pullRefreshState = rememberPullRefreshState(refreshing = onBoardingUiState.isLoading && postsUiState.isLoading, onRefresh = { fetchData() })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(state = pullRefreshState)
    ) {
        LazyColumn (
            modifier = modifier.fillMaxSize()

        ){
            if(onBoardingUiState.shouldShowOnBoarding){
                item (key = "onboardingsection"){
                    OnBoardingSection(
                        users = onBoardingUiState.users,
                        onBoardingFinish = onBoardingFinish,
                        onUserClick = onUserClick,
                        onFollowButtonClick = onFollowClick,
                    )
                }

            }

            items(postsUiState.posts, key = {post -> post.id}){ post ->
                PostListItem(
                    post = post,
                    onPostClick = onPostClick,
                    onProfileClick = onProfileClick,
                    onLikeClick = { onLikeClick(post.id) },
                    onCommentClick = { onCommentClick(post.id) }
                )
            }
        }
        PullRefreshIndicator(
            refreshing = onBoardingUiState.isLoading,
            state = pullRefreshState,
            modifier = modifier.align(Alignment.TopCenter)
        )
    }

}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(
        onBoardingUiState = OnBoardingUiState(
            users = sampleUsers,
            shouldShowOnBoarding = true,
        ),
        postsUiState = PostsUiState(
            posts = samplePosts
        ),
        onPostClick = {},
        onProfileClick = {},
        onLikeClick = {},
        onCommentClick = {},
        onBoardingFinish = {},
        onUserClick = {},
        onFollowClick = { _, _ -> },
        fetchData = {}

    )
}

