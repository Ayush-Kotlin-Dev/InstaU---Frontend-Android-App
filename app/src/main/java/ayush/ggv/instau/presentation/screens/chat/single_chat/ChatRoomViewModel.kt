package ayush.ggv.instau.presentation.screens.chat.single_chat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ayush.ggv.instau.domain.usecases.chat_service.FriendListUseCase
import ayush.ggv.instau.model.FriendList
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.data.chat.domain.ChatRepository
import ayush.ggv.instau.data.toMessage
import ayush.ggv.instau.domain.usecases.chat_service.GetRoomHistoryUseCase
import ayush.ggv.instau.model.chatRoom.MessageResponseDto
import ayush.ggv.instau.model.friendList.RoomHistoryList
import ayush.ggv.instau.util.ResponseResource
import kotlinx.coroutines.launch
import ayush.ggv.instau.util.Result
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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
    private fun pushMessageToList(message: MessageResponseDto) {
        val messageList = chatState.value.data.toMutableList().apply {
            add(0, message.toMessage())
        }
        _chatState.value = chatState.value.copy(data = messageList)
    }
    fun sendMessage() {
        viewModelScope.launch {
            if (messageText.value.isNotBlank()) {
                repository.sendMessage(messageText.value)
                _chatState.value=chatState.value.copy()
                _messageText.value = ""
            }
        }
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
    fun connectToSocket(sender: Long, receiver: Long , token : String) {
        viewModelScope.launch {
            when (val result = repository.connectToSocket(sender, receiver , token)) {
                is ResponseResource.Error -> _chatState.value =
                    ChatRoomHistoryState(error = result.errorMessage)
                is ResponseResource.Success -> {
                    repository.receiveMessage().onEach { message ->
                        pushMessageToList(message)
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

    fun disconnectSocket() {
        viewModelScope.launch { repository.disconnectSocket() }
    }
    override fun onCleared() {
        super.onCleared()
        disconnectSocket()
    }

}

data class ChatRoomHistoryState(
    val loading: Boolean = false,
    val data: List<RoomHistoryList.Message> = emptyList(),
    val error: String = ""
)

