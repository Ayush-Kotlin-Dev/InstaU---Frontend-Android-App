package ayush.ggv.instau.presentation.screens.account.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.presentation.screens.destinations.EditProfileDestination
import ayush.ggv.instau.presentation.screens.destinations.FollowersDestination
import ayush.ggv.instau.presentation.screens.destinations.FollowingDestination
import ayush.ggv.instau.presentation.screens.destinations.PostDetailDestination
import ayush.ggv.instau.presentation.screens.home.HomeScreenViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import instaU.ayush.com.model.FollowsParams
import org.koin.androidx.compose.koinViewModel


@Composable
@Destination
fun Profile(
    userId : Long ,
    navigator: DestinationsNavigator,
) {
    val viewModel : ProfileScreenViewModel = koinViewModel()

    ProfileScreen(
        userInfoUiState = viewModel.userInfoUiState,
        profilePostsUiState = viewModel.profilePostUiState,
        onButtonClick = {
            viewModel.followUnfollowUser(
                FollowsParams(
                    following = userId,
                    isFollowing = viewModel.isFollowing
                )
            )
        },
        onFollowersClick = {navigator.navigate(FollowersDestination(userId)) },
        onFollowingClick = {navigator.navigate(FollowingDestination(userId)) },
        onPostClick = {navigator.navigate(
            PostDetailDestination(
                it.postId,
            )
        )
        },
        onLikeClick = { },
        onCommentClick = { },
        fetchData = {viewModel.fetchProfile(userId )},
        navigator = navigator,
        isFollowing = viewModel.isFollowing
    )
}