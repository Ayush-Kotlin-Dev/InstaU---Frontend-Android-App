package ayush.ggv.instau.presentation.screens.chat.single_chat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ayush.ggv.instau.domain.usecases.chat_service.FriendListUseCase
import ayush.ggv.instau.model.FriendList
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.data.chat.domain.ChatRepository
import ayush.ggv.instau.domain.usecases.chat_service.GetRoomHistoryUseCase
import ayush.ggv.instau.model.friendList.RoomHistoryList
import ayush.ggv.instau.util.ResponseResource
import kotlinx.coroutines.launch
import ayush.ggv.instau.util.Result
class ChatRoomViewModel(
    private val getRoomHistoryUseCase: GetRoomHistoryUseCase,
    private val repository: ChatRepository,
) : ViewModel() {

    private val _chatState = mutableStateOf(ChatRoomHistoryState())
    val chatState: State<ChatRoomHistoryState> = _chatState

    private val _messageText = mutableStateOf("")
    val messageText: State<String> = _messageText

    fun onMessageChange(message: String) {
        _messageText.value = message
    }

    fun getChatHistory(sender : Long ,  receiver: Long , token : String) {
        _chatState.value = ChatRoomHistoryState(loading = true)
        viewModelScope.launch {
            getRoomHistoryUseCase(sender,receiver,token).collect {
                when (it) {
                    is ResponseResource.Error -> _chatState.value =
                        ChatRoomHistoryState(error = it.errorMessage.errorMessage.orEmpty())
                    is ResponseResource.Success -> _chatState.value =
                        ChatRoomHistoryState(data = it.data.roomData?.sortedByDescending { it.timestamp }
                            .orEmpty())
                }
            }
        }
    }

}

data class ChatRoomHistoryState(
    val loading: Boolean = false,
    val data: List<RoomHistoryList.Message> = emptyList(),
    val error: String = ""
)

