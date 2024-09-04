package ayush.ggv.instau.data.profile.data

import ayush.ggv.instau.data.KtorApi
import ayush.ggv.instau.data.profile.domain.model.ProfileResponse
import ayush.ggv.instau.data.profile.domain.model.UpdateUserParams
import instaU.ayush.com.model.GetFollowsResponse
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.append
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.utils.io.streams.outputStream
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
        image: ByteArray,
        updateUserParams: UpdateUserParams,
        token: String
    ): ProfileResponse = client.post {
        val jsonProfileData = Json.encodeToString(updateUserParams)
        endPoint(path = "/profile/update")
        headers {
            append("Authorization", "Bearer $token")
        }
        setBody(MultiPartFormDataContent(
            formData {
                append("profile_data", jsonProfileData)
                append("image", "image.jpg", ContentType.Application.OctetStream) {
                    outputStream().write(image)
                }
            }
        ))
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
