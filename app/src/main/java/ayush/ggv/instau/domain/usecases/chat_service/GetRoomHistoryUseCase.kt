package ayush.ggv.instau.domain.usecases.chat_service

import ayush.ggv.instau.data.chat.domain.ChatRepository
import ayush.ggv.instau.model.chatRoom.ChatRoomResponseDto
import ayush.ggv.instau.model.friendList.RoomHistoryList
import ayush.ggv.instau.util.ResponseResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetRoomHistoryUseCase : KoinComponent {
    private val repository: ChatRepository by inject()

    suspend operator fun invoke(sender: Long, receiver: Long): Flow<ResponseResource<RoomHistoryList>> = flow {
        // First, emit local data
        val localMessages = repository.getLocalMessages(sender, receiver)
        emit(ResponseResource.Success(RoomHistoryList(roomData = localMessages)))

        try {
            // Then, fetch from network and update local storage
            repository.getRoomHistory(sender, receiver).collect { response ->
                when (response) {
                    is ResponseResource.Success -> {
                        val messages = response.data.data?.filterNotNull() ?: emptyList()
                        repository.saveMessagesToLocal(messages)
                        val roomHistoryMessages = messages.map { it.toRoomHistoryMessage() }
                        val combinedMessages = (localMessages + roomHistoryMessages).distinctBy { it.timestamp }
                        emit(ResponseResource.Success(RoomHistoryList(roomData = combinedMessages)))
                    }
                    is ResponseResource.Error -> {
                        // If there's a network error, we'll just emit the local data again
                        // This ensures that the UI always has the most recent local data
                        emit(ResponseResource.Success(RoomHistoryList(roomData = localMessages)))
                    }
                }
            }
        } catch (e: Exception) {
            // If there's any exception (like no internet), we'll emit the local data
            emit(ResponseResource.Success(RoomHistoryList(roomData = localMessages)))
        }
    }
}

fun ChatRoomResponseDto.ChatRoomData.toRoomHistoryMessage() = RoomHistoryList.Message(
    sessionId = null, // ChatRoomData doesn't have sessionId
    receiver = receiver,
    sender = sender,
    textMessage = textMessage,
    timestamp = timestamp,
    formattedTime = null, // You might want to format this
    formattedDate = null  // You might want to format this
)