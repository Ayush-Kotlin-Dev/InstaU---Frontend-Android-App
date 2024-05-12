package ayush.ggv.instau.data.followunfollow.data

import ayush.ggv.instau.data.KtorApi
import ayush.ggv.instau.data.profile.domain.model.ProfileResponse
import ayush.ggv.instau.data.profile.domain.model.UpdateUserParams
import instaU.ayush.com.model.FollowsAndUnfollowsResponse
import instaU.ayush.com.model.FollowsParams
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class FollowService : KtorApi() {
    suspend fun followUser(
        followsParams : FollowsParams,
        token: String
    ): FollowsAndUnfollowsResponse = client.post {
        endPoint(path = "/follow")
        headers {
            append("Authorization", "Bearer $token")

        }
        setBody(followsParams)
    }.body()

}