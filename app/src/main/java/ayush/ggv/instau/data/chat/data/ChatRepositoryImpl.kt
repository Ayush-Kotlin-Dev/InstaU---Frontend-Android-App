package ayush.ggv.instau.data.chat.data

import ChatService
import ayush.ggv.instau.common.datastore.UserPreferences
import ayush.ggv.instau.data.chat.domain.ChatRepository
import ayush.ggv.instau.model.FriendListResponseDto
import ayush.ggv.instau.model.chatRoom.ChatRoomResponseDto
import ayush.ggv.instau.model.chatRoom.MessageResponseDto
import ayush.ggv.instau.util.ResponseResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ChatRepositoryImpl(
    private val chatService: ChatService,
    private val userPreferences: UserPreferences
) : ChatRepository {
    override suspend fun getFriendList(userId: Long): Flow<ResponseResource<FriendListResponseDto>> =
        flow {
            val userData = userPreferences.getUserData()
            val response = chatService.getFriendList(userId, userData.token)
            when (response) {
                is ResponseResource.Error -> ResponseResource.error(response.errorMessage)
                is ResponseResource.Success -> ResponseResource.success(response.data)
            }
            emit(response)
        }

    override suspend fun getRoomHistory(
        sender: Long,
        receiver: Long
    ): Flow<ResponseResource<ChatRoomResponseDto>> = flow {
        val userData = userPreferences.getUserData()
        val responseResource =
            when (val response = chatService.getRoomHistory(sender, receiver , userData.token)) {
                is ResponseResource.Error -> ResponseResource.error(response.errorMessage)
                is ResponseResource.Success -> ResponseResource.success(response.data)
            }

        emit(responseResource)
    }

    override suspend fun connectToSocket(
        sender: Long,
        receiver: Long
    ): ResponseResource<String> {
        val userData = userPreferences.getUserData()
        return when (
            val response =
            chatService.connectToSocket(sender, receiver, userData.token)) {
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