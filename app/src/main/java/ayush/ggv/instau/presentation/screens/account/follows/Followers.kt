package ayush.ggv.instau.presentation.screens.account.follows

import ayush.ggv.instau.presentation.screens.destinations.ProfileDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel


@androidx.compose.runtime.Composable
@Destination
fun Followers (
    navigator: DestinationsNavigator,
    userId: Long
){
    val viewModel: FollowsViewModel = koinViewModel()

    val currentUserid = 0L
    val token = ""
    FollowsScreen(
        uiState = viewModel.uiState,
        fetchFollows = { viewModel.fetchFollows(userId , 1)  },
        onItemClick = { navigator.navigate(ProfileDestination( userId  ))  }
    )
}
