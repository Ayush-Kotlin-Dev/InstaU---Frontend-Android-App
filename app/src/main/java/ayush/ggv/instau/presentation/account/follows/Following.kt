package ayush.ggv.instau.presentation.account.follows

import ayush.ggv.instau.presentation.destinations.Destination
import ayush.ggv.instau.presentation.destinations.ProfileDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel


@androidx.compose.runtime.Composable
@com.ramcosta.composedestinations.annotation.Destination

fun  Following (
    navigator: DestinationsNavigator,
    userId: Int
){
    val viewModel: FollowsViewModel = koinViewModel()

    FollowsScreen(
        uiState = viewModel.uiState,
        fetchFollows = { viewModel.fetchFollows(userId , 2)  },
        onItemClick = { navigator.navigate(ProfileDestination(it)) }
    )
}