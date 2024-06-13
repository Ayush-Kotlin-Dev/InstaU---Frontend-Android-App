import android.util.Log
import ayush.ggv.instau.data.KtorApi
import ayush.ggv.instau.model.FriendListResponseDto
import ayush.ggv.instau.util.ResponseResource
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter

class ChatService : KtorApi() {
    suspend fun getFriendList(
        userId: Long,
        token: String
    ): ResponseResource<FriendListResponseDto> =
        try {
            val response = client.get {
                endPoint(path = "/chat/friends-list")
                parameter("sender", userId)
                headers {
                    append("Authorization", "Bearer $token")
                }
            }.body<FriendListResponseDto>()
            Log.d("ChatService", "getFriendList: ${response.data}")

            when (response.data) {
                null -> ResponseResource.error(response)
                else -> ResponseResource.success(response)
            }
        } catch (e: Exception) {
            Log.d("ChatService", "getFriendList: ${e.message}")
            ResponseResource.error(
                FriendListResponseDto(
                    error = FriendListResponseDto.Error("Oops, something bad happened :(")
                )
            )
        }
}
