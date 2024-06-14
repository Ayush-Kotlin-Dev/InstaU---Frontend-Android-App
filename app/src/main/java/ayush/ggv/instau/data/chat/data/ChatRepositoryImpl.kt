package ayush.ggv.instau.data.chat.data

import ChatService
import ayush.ggv.instau.data.chat.domain.ChatRepository
import ayush.ggv.instau.model.FriendListResponseDto
import ayush.ggv.instau.model.chatRoom.ChatRoomResponseDto
import ayush.ggv.instau.model.chatRoom.MessageResponseDto
import ayush.ggv.instau.util.ResponseResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ChatRepositoryImpl(
    private val chatService: ChatService
) : ChatRepository {
    override suspend fun getFriendList(userId: Long, token: String): Flow<ResponseResource<FriendListResponseDto>> =
        flow {
            val response = chatService.getFriendList(userId, token)
            when (response) {
                is ResponseResource.Error -> ResponseResource.error(response.errorMessage)
                is ResponseResource.Success -> ResponseResource.success(response.data)
            }
            emit(response)
        }

    override suspend fun getRoomHistory(
        sender: Long,
        receiver: Long,
        token : String
    ): Flow<ResponseResource<ChatRoomResponseDto>> = flow {
        val responseResource =
            when (val response = chatService.getRoomHistory(sender, receiver , token)) {
                is ResponseResource.Error -> ResponseResource.error(response.errorMessage)
                is ResponseResource.Success -> ResponseResource.success(response.data)
            }

        emit(responseResource)
    }

    override suspend fun connectToSocket(
        sender: Long,
        receiver: Long,
        token: String
    ): ResponseResource<String> {
        return when (val response =
            chatService.connectToSocket(sender, receiver, token)) {
            is ResponseResource.Error -> ResponseResource.error(response.errorMessage)
            is ResponseResource.Success -> ResponseResource.success(response.data)
        }
    }

    override suspend fun sendMessage(message: String) {
        chatService.sendMessage(message)
    }

    override fun receiveMessage(): Flow<MessageResponseDto> = chatService.receiveMessage()


    override suspend fun disconnectSocket() {
        chatService.disconnectSocket()
    }
}