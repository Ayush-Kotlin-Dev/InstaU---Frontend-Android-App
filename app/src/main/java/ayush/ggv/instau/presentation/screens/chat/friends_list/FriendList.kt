package ayush.ggv.instau.presentation.screens.chat.friends_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import ayush.ggv.instau.presentation.screens.destinations.ChatRoomDestination
import ayush.ggv.instau.presentation.screens.destinations.TestChatScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel


@Destination
@Composable
fun FriendList(
    navigator: DestinationsNavigator,
) {
    val viewModel: FriendListScreenViewModel = koinViewModel()
    val friendListState = viewModel.friendListState.value
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.getFriendList()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    FriendListScreen(
        friendListState = friendListState,
        onNavigateToChatScreen = { friendId, friendName, friendAvatar ->
            navigator.navigate(ChatRoomDestination(
                friendId,
                friendName,
                friendAvatar,
                viewModel.userAvatar.value,
                viewModel.currentUserId.longValue,
                viewModel.token.value
            ).route)
        },
        onGroupChatClick = { navigator.navigate(TestChatScreenDestination) }


    )

}