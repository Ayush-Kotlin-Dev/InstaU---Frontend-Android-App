package ayush.ggv.instau.data.profile.data

import ayush.ggv.instau.data.KtorApi
import ayush.ggv.instau.data.profile.domain.model.ProfileResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter

class ProfileService : KtorApi() {
    suspend fun getUserProfile(
        userId: Long,
        currentUserId: Long,
        token: String
    ): ProfileResponse {
        val response = client.get {
            endPoint(path = "/profile/$userId")
            headers {
                append("Authorization", "Bearer $token")
            }
            parameter("currentUserId", currentUserId)
        }
        return response.body<ProfileResponse>()
    }
}