package ayush.ggv.instau.presentation.screens.chat

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun Main_ChatScreen(

) {
    val viewModel : ChatViewModel = koinViewModel()

    val state = viewModel.state
    if(state.isEnteringToken) {
        // Enter Token Screen
        EnterTokenDialog(
            token = state.remoteToken,
            onTokenChanged = viewModel::onRemoteTokenChanged,
        ) {
            viewModel.onSubmitRemoteToken()
        }
    }else{
        // Chat Screen
        ChatScreen(
            messageText = state.messageText,
            onMessageChange =  viewModel::onMessageChanged,
            onMessageSend = { viewModel.sendMessage(false) },
            onMessageBroadcast = { viewModel.sendMessage(true) }
        )
    }

}