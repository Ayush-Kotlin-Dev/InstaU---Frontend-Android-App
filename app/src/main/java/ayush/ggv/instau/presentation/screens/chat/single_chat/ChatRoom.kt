package ayush.ggv.instau.presentation.screens.chat.single_chat

import androidx.compose.runtime.Composable
import ayush.ggv.instau.presentation.screens.chat.single_chat.ChatRoomScreen
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun ChatRoom(
     token : String,
     userId : Long,
) {

    ChatRoomScreen()

}