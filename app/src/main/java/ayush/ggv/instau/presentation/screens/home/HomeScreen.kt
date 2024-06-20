package ayush.ggv.instau.presentation.screens.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import ayush.ggv.instau.R
import ayush.ggv.instau.model.Post
import ayush.ggv.instau.presentation.common.EmptyScreen
import ayush.ggv.instau.presentation.components.PostListItem
import ayush.ggv.instau.presentation.components.ShimmerEffect
import ayush.ggv.instau.presentation.screens.account.profile.ProfileScreenViewModel
import ayush.ggv.instau.presentation.screens.home.onboarding.OnBoardingSection
import ayush.ggv.instau.presentation.screens.home.onboarding.OnBoardingUiState
import ayush.ggv.instau.ui.theme.LargeSpacing
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt


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
    profileScreenViewModel: ProfileScreenViewModel,
    currentUserId: Long,
    token: String,
    newPostsAvailable: Boolean,
    onDismiss: () -> Unit
) {
    val post = postsUiState.currentPostResult?.collectAsLazyPagingItems()
    val result = handlePagingResult(posts = post ?: return)
    if (!result) {
        return
    }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = onBoardingUiState.isLoading && postsUiState.isLoading,
        onRefresh = {
            post.refresh()
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(state = pullRefreshState)
    ) {

        LazyColumn(
            modifier = modifier.fillMaxSize(),
            content = {
                if (onBoardingUiState.shouldShowOnBoarding) {
                    item(key = "onboardingsection") {
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

        )
        // Show the NewPostsOverlayButton if new posts are available
        if (newPostsAvailable) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                NewPostsOverlayButton(onClick = {
                    post.refresh()
                }, onDismiss = onDismiss)
            }
        }
        PullRefreshIndicator(
            refreshing = onBoardingUiState.isLoading,
            state = pullRefreshState,
            contentColor = androidx.compose.ui.graphics.Color.Blue,
            modifier = modifier.align(Alignment.TopCenter)
        )
    }

}

@Composable
fun handlePagingResult(
    posts: LazyPagingItems<Post>,
): Boolean {
    posts.apply {
        val error = when {
            loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
            loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
            loadState.append is LoadState.Error -> loadState.append as LoadState.Error
            else -> null
        }
        return when {
            loadState.refresh is LoadState.Loading -> {
                ShimmerEffect()
                false
            }

            error != null -> {

                EmptyScreen(error = error, posts = posts)
                false
            }

            posts.itemCount < 1 -> {
                EmptyScreen()
                false
            }

            else -> true // Success
        }
    }

}


@Composable
fun NewPostsOverlayButton(
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    OutlinedButton(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        coroutineScope.launch {
                            if (abs(offsetX.value) > 150f) {
                                // If the button is dragged horizontally enough, animate it out of view and then dismiss
                                offsetX.animateTo(
                                    targetValue = if (offsetX.value > 0) 200f else -200f,
                                    animationSpec = TweenSpec(durationMillis = 300)
                                )
                                onDismiss()
                            } else {
                                // Otherwise, animate back to the original position
                                offsetX.animateTo(
                                    targetValue = 0f,
                                    animationSpec = TweenSpec(durationMillis = 300)
                                )
                            }
                        }
                    }
                ) { change, dragAmount ->
                    change.consume()
                    coroutineScope.launch {
                        offsetX.snapTo(offsetX.value + dragAmount)
                    }
                }
            }
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
            .padding(horizontal = 80.dp),
        shape = RoundedCornerShape(50),
    ) {
        Text(text = stringResource(id = R.string.newPost))
    }
}