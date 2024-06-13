package ayush.ggv.instau.data.chat.data

import android.util.Log
import ayush.ggv.instau.data.chat.domain.ChatRepository
import ayush.ggv.instau.model.FriendListResponseDto
import ayush.ggv.instau.util.Result

class ChatRepositoryImpl(
    private val chatService: ChatService
) : ChatRepository {
    override suspend fun getFriendList(userId: Long, token: String): Result<FriendListResponseDto> {
        return try {
            val response = chatService.getFriendList(userId, token)
            Log.d("ChatRepositoryImpl", "getFriendListSuceess: ${response.data}")
            Result.Success(response)
        } catch (e: Exception) {
            Log.d("ChatRepositoryImpl", "getFriendList: ${e.toString()}")
            Result.Error(e.toString())

        }

    }
}