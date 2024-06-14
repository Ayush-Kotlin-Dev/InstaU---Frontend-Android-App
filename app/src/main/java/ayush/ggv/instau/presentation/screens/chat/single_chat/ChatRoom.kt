package ayush.ggv.instau.presentation.screens.chat.single_chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun ChatRoom(
    friendId: Long,
    friendName: String,
    friendAvatar: String,
    userAvatar: String,
    userId: Long,
    token: String,
    ) {

    val viewModel: ChatRoomViewModel = koinViewModel()
    val chatState = viewModel.chatState.value

    LaunchedEffect(key1 = true) {
        viewModel.getChatHistory(userId , friendId , token)
    }
    ChatRoomScreen(
        friendName = friendName,
        friendAvatar = friendAvatar,
        userAvatar = userAvatar,
        userId = userId,
        chatState = chatState,
        messageText = viewModel.messageText.value,
        onMessageChange = viewModel::onMessageChange
    )

}