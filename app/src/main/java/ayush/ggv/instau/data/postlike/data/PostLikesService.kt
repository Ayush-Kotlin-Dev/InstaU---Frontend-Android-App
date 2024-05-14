package ayush.ggv.instau.data.postlike.data

import ayush.ggv.instau.data.KtorApi
import ayush.ggv.instau.model.PostsResponse
import instaU.ayush.com.model.LikeParams
import instaU.ayush.com.model.LikeResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody

class PostService : KtorApi() {
    suspend fun addLike(
        likeParams: LikeParams,
        token: String
    ): LikeResponse = client.get {
        endPoint(path = "/posts/likes/add")

        headers {
            append("Authorization", "Bearer $token")
        }
        setBody(likeParams)
    }.body()


    suspend fun removeLike(
        likeParams: LikeParams,
        token: String
    ): LikeResponse = client.get {
        endPoint(path = "/posts/likes/remove")
        headers {
            append("Authorization", "Bearer $token")
        }
        setBody(likeParams)
    }.body()
}
