package ayush.ggv.instau.presentation.screens.account.follows

import android.util.Log
import ayush.ggv.instau.presentation.screens.destinations.Destination
import ayush.ggv.instau.presentation.screens.destinations.ProfileDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel


@androidx.compose.runtime.Composable
@com.ramcosta.composedestinations.annotation.Destination

fun  Following (
    navigator: DestinationsNavigator,
    userId: Long,
){
    val viewModel: FollowsViewModel = koinViewModel()

    FollowsScreen(
        uiState = viewModel.uiState,
        fetchFollows = { viewModel.fetchFollows(userId )  },
        onItemClick = {selectedUserId -> navigator.navigate(ProfileDestination( selectedUserId)) } ,
        isFollowers = false
    )
}