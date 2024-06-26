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
import ayush.ggv.instau.presentation.screens.home.HomeScreenViewModel
import ayush.ggv.instau.util.Utils
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import instaU.ayush.com.model.FollowsParams
import org.koin.androidx.compose.koinViewModel


@Composable
@Destination
fun Profile(
    userId : Long ,
    currentUserId : Long,
    token : String,
    navigator: DestinationsNavigator,
) {
    val viewModel : ProfileScreenViewModel = koinViewModel()

    ProfileScreen(
        userInfoUiState = viewModel.userInfoUiState,
        profilePostsUiState = viewModel.profilePostUiState,
        onButtonClick = {
            viewModel.followUnfollowUser(
                FollowsParams(
                    follower = currentUserId,
                    following = userId,
                    isFollowing = viewModel.isFollowing
                ),
                token
            )
        },
        onFollowersClick = {navigator.navigate(FollowersDestination(userId , currentUserId , token)) },
        onFollowingClick = {navigator.navigate(FollowingDestination(userId , currentUserId ,token)) },
        onPostClick = { },
        onLikeClick = { },
        onCommentClick = { },
        fetchData = {viewModel.fetchProfile(userId , currentUserId , token )},
        navigator = navigator,
        token = token,
        isFollowing = viewModel.isFollowing
    )
}