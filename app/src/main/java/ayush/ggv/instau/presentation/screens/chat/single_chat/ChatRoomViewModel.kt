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
import ayush.ggv.instau.model.chatRoom.ChatRoomResponseDto
import ayush.ggv.instau.model.chatRoom.MessageResponseDto
import ayush.ggv.instau.model.friendList.RoomHistoryList
import ayush.ggv.instau.util.ResponseResource
import kotlinx.coroutines.launch
import ayush.ggv.instau.util.Result
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.Locale

class ChatRoomViewModel(
    private val getRoomHistoryUseCase: GetRoomHistoryUseCase,
    private val repository: ChatRepository,
) : ViewModel() {

    private val _chatState = mutableStateOf(ChatRoomHistoryState())
    val chatState: State<ChatRoomHistoryState> = _chatState

    private val _messageText = mutableStateOf("")
    val messageText: State<String> = _messageText

    private var isSocketConnected = false

    fun onMessageChange(message: String) {
        _messageText.value = message
    }

    private fun pushMessageToList(message: MessageResponseDto) {
        val messageList = chatState.value.data.toMutableList()
        val existingMessage = messageList.find { it.timestamp == message.timestamp }
        if (existingMessage == null) {
            messageList.add(0, message.toMessage())
            _chatState.value = chatState.value.copy(data = messageList)
        }
    }

    fun getChatHistory(sender: Long, receiver: Long) {
        _chatState.value = ChatRoomHistoryState(loading = true, sender = sender, receiver = receiver)
        viewModelScope.launch {
            getRoomHistoryUseCase(sender, receiver).collect { result ->
                when (result) {
                    is ResponseResource.Error -> _chatState.value = ChatRoomHistoryState(error = result.errorMessage.toString(), sender = sender, receiver = receiver)
                    is ResponseResource.Success -> {
                        val currentData = _chatState.value.data
                        val newData = result.data.roomData.orEmpty()
                        val combinedData = (currentData + newData).distinctBy { it.timestamp }
                        _chatState.value = ChatRoomHistoryState(data = combinedData, sender = sender, receiver = receiver)
                    }
                }
            }
        }
    }

    fun sendMessage() {
        viewModelScope.launch {
            if (messageText.value.isNotBlank()) {
                val newMessage = RoomHistoryList.Message(
                    sender = chatState.value.sender,
                    receiver = chatState.value.receiver,
                    textMessage = messageText.value,
                    timestamp = System.currentTimeMillis().toString(),
                    formattedTime = getCurrentFormattedTime(),
                    formattedDate = getCurrentFormattedDate()
                )
                repository.saveMessagesToLocal(listOf(newMessage.toChatRoomData()))
                if (isSocketConnected) {
                    repository.sendMessage(messageText.value)
                } else {
                    // Handle offline message sending
                    _chatState.value = chatState.value.copy(
                        data = listOf(newMessage) + chatState.value.data
                    )
                }
                _messageText.value = ""
            }
        }
    }

    private fun getCurrentFormattedTime(): String {
        val currentTime = System.currentTimeMillis()
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return timeFormat.format(currentTime)
    }

    private fun getCurrentFormattedDate(): String {
        val currentTime = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(currentTime)
    }

    fun connectToSocket(sender: Long, receiver: Long) {
        viewModelScope.launch {
            when (val result = repository.connectToSocket(sender, receiver)) {
                is ResponseResource.Error -> {
                    _chatState.value = chatState.value.copy(error = result.errorMessage)
                    isSocketConnected = false
                }
                is ResponseResource.Success -> {
                    _chatState.value = chatState.value.copy(sender = sender, receiver = receiver)
                    isSocketConnected = true
                    repository.receiveMessage().onEach { message ->
                        pushMessageToList(message)
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

    fun disconnectSocket() {
        viewModelScope.launch {
            repository.disconnectSocket()
            isSocketConnected = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnectSocket()
    }
}

data class ChatRoomHistoryState(
    val loading: Boolean = false,
    val sender: Long = -1,
    val receiver: Long = -1,
    val data: List<RoomHistoryList.Message> = emptyList(),
    val error: String = ""
)

fun RoomHistoryList.Message.toChatRoomData() = ChatRoomResponseDto.ChatRoomData(
    sender = sender,
    receiver = receiver,
    textMessage = textMessage,
    timestamp = timestamp
)

fun MessageResponseDto.toRoomHistoryMessage() = RoomHistoryList.Message(
    sessionId = sessionId,
    receiver = receiver,
    sender = sender,
    textMessage = textMessage,
    timestamp = timestamp,
    formattedTime = null,
    formattedDate = null
)