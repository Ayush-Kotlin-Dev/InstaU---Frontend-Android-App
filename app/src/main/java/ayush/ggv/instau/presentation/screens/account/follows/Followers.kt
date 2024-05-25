package ayush.ggv.instau.presentation.screens.account.follows

import android.util.Log
import androidx.paging.compose.collectAsLazyPagingItems
import ayush.ggv.instau.presentation.screens.destinations.ProfileDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel


@androidx.compose.runtime.Composable
@Destination
fun Followers (
    navigator: DestinationsNavigator,
    userId: Long,
    currentUserid: Long,
    token : String
){
    val viewModel: FollowsViewModel = koinViewModel()


    FollowsScreen(
        uiState = viewModel.uiState,
        fetchFollows = { viewModel.fetchFollows(userId , token)  },
        onItemClick = { selectedUserId -> navigator.navigate(ProfileDestination( selectedUserId , currentUserid, token ))  },
        isFollowers = true
    )
}
