package ayush.ggv.instau.presentation.screens.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.data.notification.NotificationService
import ayush.ggv.instau.model.notification.ChatState
import ayush.ggv.instau.model.notification.NotificationBody
import ayush.ggv.instau.model.notification.SendMessageDto
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ChatViewModel(
    private val notificationService: NotificationService
) : ViewModel() {

    var state by mutableStateOf(ChatState())
        private set
    init {
        viewModelScope.launch {
            Firebase.messaging.subscribeToTopic("chat").await()
        }
    }
    fun onRemoteTokenChanged(remoteToken: String) {
        state = state.copy(
            remoteToken = remoteToken
        )
    }
    fun onSubmitRemoteToken() {
        state = state.copy(
            isEnteringToken = false
        )
    }
    fun onMessageChanged(message: String) {
        state = state.copy(
            messageText = message
        )
    }

    fun sendMessage(isBroadcast : Boolean) {
       viewModelScope.launch {
           val messageDto = SendMessageDto(
               to = if (isBroadcast) null else state.remoteToken,
               notification = NotificationBody(
                   title = "New Message",
                   body = state.messageText
               )
           )
           try {
               if (isBroadcast) {
                   notificationService.broadcastMessage(messageDto)
               } else {
                   notificationService.sendMessage(messageDto)
               }

               state = state.copy(
                   messageText = ""
               )
           } catch (e: Exception) {
               e.printStackTrace()
           }
       }
    }


}