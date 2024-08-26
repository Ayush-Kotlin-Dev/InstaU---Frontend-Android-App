package ayush.ggv.instau.presentation.screens.chat.single_chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun ChatRoom(
    navigator: DestinationsNavigator,
    friendId: Long,
    friendName: String,
    friendAvatar: String,
    userAvatar: String,
    userId: Long
) {

    val viewModel: ChatRoomViewModel = koinViewModel()
    val chatState = viewModel.chatState.value
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = true) {
        viewModel.getChatHistory(userId , friendId )
    }

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START)
                viewModel.connectToSocket(
                    sender = userId,
                    receiver = friendId,
                )
            else if (event == Lifecycle.Event.ON_STOP)
                viewModel.disconnectSocket()
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // State to handle typing status
    var isTyping by remember { mutableStateOf(false) }

    // Update the isTyping state every 5 seconds
    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(5000) // Delay for 5 seconds
            isTyping = kotlin.random.Random.nextBoolean()
        }
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
        onBackClick = { navigator.navigateUp() },
//        isTyping = isTyping
    )
}
