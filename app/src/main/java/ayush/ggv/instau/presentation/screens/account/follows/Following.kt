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
    currentUserid: Long,
    token : String
){
    val viewModel: FollowsViewModel = koinViewModel()

    FollowsScreen(
        uiState = viewModel.uiState,
        fetchFollows = { viewModel.fetchFollows(userId , 1 , 10, token)  },
        onItemClick = {selectedUserId -> navigator.navigate(ProfileDestination( selectedUserId , currentUserid, token )) } ,
        isFollowers = false
    )
}