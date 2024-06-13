package ayush.ggv.instau.data.chat.data

import ayush.ggv.instau.data.KtorApi
import ayush.ggv.instau.model.FriendListResponseDto
import instaU.ayush.com.model.FollowsAndUnfollowsResponse
import instaU.ayush.com.model.FollowsParams
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody


class ChatService : KtorApi() {
    suspend fun getFriendList(
        userId : Long,
        token: String
    ): FriendListResponseDto = client.get {
        endPoint(path = "/chat/friends-list")
        parameter("sender", userId)
        headers {
            append("Authorization", "Bearer $token")

        }
    }.body()

}