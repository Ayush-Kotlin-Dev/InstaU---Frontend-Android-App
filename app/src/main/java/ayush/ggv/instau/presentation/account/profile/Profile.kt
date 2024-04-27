package ayush.ggv.instau.presentation.account.profile

import androidx.compose.runtime.Composable
import ayush.ggv.instau.presentation.destinations.EditProfileDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel


@Composable
@Destination
fun Profile(
    userId : Int,
    navigator: DestinationsNavigator
) {
    val viewModel : ProfileScreenViewModel = koinViewModel()

    viewModel.fetchProfile(userId)

    ProfileScreen(
        userInfoUiState = viewModel.userInfoUiState,
        profilePostsUiState = viewModel.profilePostUiState,
        onButtonClick = { navigator.navigate(EditProfileDestination(userId))},
        onFollowersClick = {},
        onFollowingClick = {},
        onPostClick = {},
        onLikeClick = {},
        onCommentClick = {},
        fetchData = {viewModel.fetchProfile(userId)}
    )

}