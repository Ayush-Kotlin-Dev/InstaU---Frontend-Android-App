package ayush.ggv.instau.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import ayush.ggv.instau.R
import ayush.ggv.instau.model.Post
import ayush.ggv.instau.presentation.components.PostListItem
import ayush.ggv.instau.presentation.components.ShimmerPostListItemPlaceholder
import ayush.ggv.instau.presentation.screens.account.profile.ProfileScreenViewModel
import ayush.ggv.instau.presentation.screens.home.onboarding.OnBoardingSection
import ayush.ggv.instau.presentation.screens.home.onboarding.OnBoardingUiState
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onBoardingUiState: OnBoardingUiState,
    postsUiState: PostsUiState,
    onPostClick: (Post) -> Unit,
    onProfileClick: (Long) -> Unit,
    onLikeClick: (String) -> Unit,
    onCommentClick: (String) -> Unit,

    //onboarding
    onBoardingFinish: () -> Unit,
    onUserClick: (Long) -> Unit,
    onFollowClick: (Long) -> Unit,

    fetchData: () -> Unit,
    profileScreenViewModel: ProfileScreenViewModel,
    currentUserId: Long,
    token : String,
    post : LazyPagingItems<Post> ?= null
) {

    val pullRefreshState = rememberPullRefreshState(
        refreshing = onBoardingUiState.isLoading && postsUiState.isLoading,
        onRefresh = { fetchData() })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(state = pullRefreshState)
    ) {

        LazyColumn(
            modifier = modifier.fillMaxSize(),
            content= {
                if(onBoardingUiState.shouldShowOnBoarding){
                    item (key = "onboardingsection"){
                        OnBoardingSection(
                            users = onBoardingUiState.users,
                            onBoardingFinish = onBoardingFinish,
                            onUserClick = onUserClick,
                            onFollowButtonClick = {},
                            profileScreenViewModel = profileScreenViewModel,
                            currentUserId = currentUserId,
                            token = token
                        )
                    }

                }

                if(  post== null && !postsUiState.isLoading){
                    item (key = "empty"){
                        Box(modifier = Modifier.fillMaxWidth() , contentAlignment = Alignment.Center){
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Spacer(modifier = Modifier.height(150.dp))
                                AsyncImage(model = R.drawable.network_error, contentDescription =null  , modifier = Modifier
                                    .size(200.dp)
                                    .alpha(0.3f)
                                )
                                Text(text = "SomeThings Went Wrong " , style = MaterialTheme.typography.button , modifier = Modifier
                                    .padding(8.dp)
                                    .alpha(0.3f)
                                )
                                Text(text = "Please Refresh the feed " , style = MaterialTheme.typography.button , modifier = Modifier
                                    .padding(8.dp)
                                    .alpha(0.3f)
                                )
                            }

                        }

                    }
                }
                if (postsUiState.isLoading) {
                    // Display a list of shimmer placeholders when loading
                    items(5) { // Replace 5 with the number of placeholders you want to show
                        ShimmerPostListItemPlaceholder()
                    }
                } else {

                    if (post != null) {

                        items(
                            items = post,
                            key = { post -> post.postId.toString() }
                        ) { index ->
                            index?.let {
                                PostListItem(
                                    post = it,
                                    onPostClick = onPostClick,
                                    onProfileClick = onProfileClick,
                                    onLikeClick = { onLikeClick(it.postId.toString()) },
                                    onCommentClick = { },
                                )
                            }
                        }
                    }
                }
            }

        )
        PullRefreshIndicator(
            refreshing = onBoardingUiState.isLoading,
            state = pullRefreshState,
            modifier = modifier.align(Alignment.TopCenter)
        )
    }

}