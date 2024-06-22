package ayush.ggv.instau.data.profile.data

import ayush.ggv.instau.data.KtorApi
import ayush.ggv.instau.data.profile.domain.model.ProfileResponse
import ayush.ggv.instau.data.profile.domain.model.UpdateUserParams
import instaU.ayush.com.model.GetFollowsResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.Serializable

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

    suspend fun updateUserProfile(
        updateUserParams: UpdateUserParams,
        token: String
    ): ProfileResponse = client.post {
        endPoint(path = "/profile/update")
        headers {
            append("Authorization", "Bearer $token")
        }
        setBody(updateUserParams)
    }.body()

    suspend fun searchUsersByName(
        name: String,
        token: String
    ): GetFollowsResponse = client.get {
        endPoint(path = "/profile/search")
        headers {
            append("Authorization", "Bearer $token")
        }
        parameter("name", name)
    }.body()

    suspend fun storeToken(
        userId: Long,
        token: String
    ): HttpResponse = client.post {
        endPoint(path = "/fcm/store/store")
        setBody(StoreFcmTokenRequest(userId, token))

    }.body()

}

@Serializable
data class StoreFcmTokenRequest(
    val userId: Long,
    val token: String
)
