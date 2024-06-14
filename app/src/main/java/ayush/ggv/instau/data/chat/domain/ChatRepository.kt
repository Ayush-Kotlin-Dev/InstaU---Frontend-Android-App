package ayush.ggv.instau.data.chat.domain

import ayush.ggv.instau.model.FriendListResponseDto
import ayush.ggv.instau.model.chatRoom.ChatRoomResponseDto
import ayush.ggv.instau.model.chatRoom.MessageResponseDto
import ayush.ggv.instau.util.ResponseResource
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.FollowsAndUnfollowsResponse
import instaU.ayush.com.model.FollowsParams
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getFriendList(
        userId: Long,
        token: String
    ): Flow<ResponseResource<FriendListResponseDto>>

    suspend fun getRoomHistory(
        sender : Long,
        receiver : Long,
        token : String
    ): Flow<ResponseResource<ChatRoomResponseDto>>
    suspend fun connectToSocket(sender: Long, receiver: Long , token : String): ResponseResource<String>

    suspend fun sendMessage(message: String)
    fun receiveMessage(): Flow<MessageResponseDto>
    suspend fun disconnectSocket()

}