package ayush.ggv.instau.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.paging.compose.collectAsLazyPagingItems
import ayush.ggv.instau.presentation.screens.account.profile.ProfileScreenViewModel
import ayush.ggv.instau.presentation.screens.destinations.PostDetailDestination
import ayush.ggv.instau.presentation.screens.destinations.ProfileDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import instaU.ayush.com.model.FollowsParams
import org.koin.androidx.compose.koinViewModel

@SuppressLint("SuspiciousIndentation")
@Composable
@Destination(start = true)

fun Home(
    navigator: DestinationsNavigator
) {
    val viewModel: HomeScreenViewModel = koinViewModel()
    val currentUserId = viewModel.currentUserId
    val token = viewModel.token
    val lifecycleOwner = LocalLifecycleOwner.current
    val profileScreenViewModel: ProfileScreenViewModel = koinViewModel()
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START)
                viewModel.connectToSocket()
            else if (event == Lifecycle.Event.ON_STOP)
                viewModel.disconnectSocket()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

        HomeScreen(
            onBoardingUiState = viewModel.onBoardingUiState,
            postsUiState = viewModel.postsUiState,

            onPostClick = {
                navigator.navigate(
                    PostDetailDestination(
                        it.postId,
                        currentUserId.value,
                        token.value
                    )
                )

            },
            onProfileClick = { userId ->
                navigator.navigate(ProfileDestination(userId, currentUserId.value, token.value))
            },
            onLikeClick = { postId ->

            },
            onCommentClick = { postId ->

            },
            onBoardingFinish = {

            },
            onUserClick = { userId ->
                navigator.navigate(ProfileDestination(userId, currentUserId.value, token.value))
            },

            onFollowClick = {
                currentUserId.value
            },
            profileScreenViewModel = profileScreenViewModel,
            currentUserId = currentUserId.value,
            token = token.value,
            newPostsAvailable = viewModel.newPostsAvailable,
            onDismiss = {
                viewModel.newPostsAvailable = false
            }
        )


}