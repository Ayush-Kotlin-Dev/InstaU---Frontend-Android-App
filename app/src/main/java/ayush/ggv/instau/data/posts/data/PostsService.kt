package ayush.ggv.instau.data.posts.data

import ayush.ggv.instau.data.KtorApi
import ayush.ggv.instau.data.posts.domain.model.PostResultData
import ayush.ggv.instau.model.PostResponse
import ayush.ggv.instau.model.PostsResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
class PostService : KtorApi() {
    suspend fun getFeedPosts(currentUserId: Long, page: Int, limit: Int, token: String): PostsResponse {
        val response = client.get {
            endPoint(path = "/posts/feed")
            parameter("currentUserId", currentUserId)
            parameter("page", page)
            parameter("limit", limit)
            headers {
                append("Authorization", "Bearer $token")
            }
        }
        return response.body<PostsResponse>()
    }
}