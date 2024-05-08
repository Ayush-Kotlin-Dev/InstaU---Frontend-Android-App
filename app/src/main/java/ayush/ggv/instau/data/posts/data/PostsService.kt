package ayush.ggv.instau.data.posts.data

import ayush.ggv.instau.data.KtorApi
import ayush.ggv.instau.data.posts.domain.model.PostResultData
import ayush.ggv.instau.model.PostResponse
import ayush.ggv.instau.model.PostTextParams
import ayush.ggv.instau.model.PostsResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class PostService : KtorApi() {
    suspend fun getFeedPosts(
        currentUserId: Long,
        page: Int,
        limit: Int,
        token: String
    ): PostsResponse {
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

    suspend fun createPost(
        postTextParams: PostTextParams,
        token: String
    ): PostResponse {
        val response = client.post {
            endPoint(path = "/post/create")
            headers {
                append("Authorization", "Bearer ${token}")
                setBody(postTextParams)
            }
        }
        return response.body<PostResponse>()

    }

    suspend fun getPost(
        postId: Long,
        currentUserId: Long?,
        token: String
    ): PostResponse {
        val response = client.get {
            endPoint(path = "/post/$postId")
            currentUserId?.let { parameter("currentUserId", it) }
            headers {
                append("Authorization", "Bearer $token")
            }
        }
        return response.body<PostResponse>()
    }
}