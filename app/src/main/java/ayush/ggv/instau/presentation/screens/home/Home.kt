package ayush.ggv.instau.presentation.screens.home

import androidx.compose.runtime.Composable
import androidx.paging.compose.collectAsLazyPagingItems
import ayush.ggv.instau.presentation.screens.account.profile.ProfileScreenViewModel
import ayush.ggv.instau.presentation.screens.destinations.PostDetailDestination
import ayush.ggv.instau.presentation.screens.destinations.ProfileDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import instaU.ayush.com.model.FollowsParams
import org.koin.androidx.compose.koinViewModel

@Composable
@Destination(start = true)

fun Home(
    navigator: DestinationsNavigator
) {
    val viewModel: HomeScreenViewModel = koinViewModel()
    val currentUserId = viewModel.currentUserId
    val token = viewModel.token

    val profileScreenViewModel: ProfileScreenViewModel = koinViewModel()
    val posts = viewModel.postsUiState.currentPostResult?.collectAsLazyPagingItems()

    HomeScreen(
        onBoardingUiState = viewModel.onBoardingUiState,
        postsUiState = viewModel.postsUiState,

        onPostClick = {
            navigator.navigate(PostDetailDestination(  it.postId , currentUserId.value, token.value ))

        },
        onProfileClick = { userId ->
            navigator.navigate(ProfileDestination(userId , currentUserId.value    , token.value))
        },
        onLikeClick = { postId ->

        },
        onCommentClick = { postId ->

        },
        onBoardingFinish = {

        },
        onUserClick = { userId ->
            navigator.navigate(ProfileDestination(userId , currentUserId.value    , token.value))
        },

        onFollowClick = {
            currentUserId.value
        },
        fetchData = {
            viewModel.fetchData()
        },
        profileScreenViewModel = profileScreenViewModel,
        currentUserId = currentUserId.value,
        token = token.value,
        post = posts
    )
}