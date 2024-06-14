package ayush.ggv.instau.presentation.screens.chat.single_chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun ChatRoom(
    navigator: DestinationsNavigator,
    friendId: Long,
    friendName: String,
    friendAvatar: String,
    userAvatar: String,
    userId: Long,
    token: String,
    ) {

    val viewModel: ChatRoomViewModel = koinViewModel()
    val chatState = viewModel.chatState.value
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = true) {
        viewModel.getChatHistory(userId , friendId , token)
    }


    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START)
                viewModel.connectToSocket(
                    sender = userId,
                    receiver = friendId,
                    token = token
                )
            else if (event == Lifecycle.Event.ON_STOP)
                viewModel.disconnectSocket()
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    ChatRoomScreen(
        friendName = friendName,
        friendAvatar = friendAvatar,
        userAvatar = userAvatar,
        userId = userId,
        chatState = chatState,
        messageText = viewModel.messageText.value,
        onMessageChange = viewModel::onMessageChange,
        onSendClick = viewModel::sendMessage,
        onBackClick = { navigator.navigateUp() }
    )

}