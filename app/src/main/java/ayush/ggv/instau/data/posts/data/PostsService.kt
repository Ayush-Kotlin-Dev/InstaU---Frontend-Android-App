package ayush.ggv.instau.data.posts.data

import android.util.Log
import ayush.ggv.instau.data.KtorApi
import ayush.ggv.instau.model.PostResponse
import ayush.ggv.instau.model.PostTextParams
import ayush.ggv.instau.model.PostsResponse
import ayush.ggv.instau.util.ResponseResource
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readText
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json

class PostService : KtorApi() {
    private var webSocket: WebSocketSession? = null

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
        Log.d("PostsRemoteMediator", "getFeedPosts: page $page ${response.bodyAsText()}" )
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

    suspend fun getPostsByUser(
        userId: Long,
        currentUserId: Long,
        pageNumber: Int,
        pageSize: Int,
        token : String
    ):PostsResponse{
        val response = client.get {
            endPoint(path = "/posts/$userId")
            parameter("currentUserId", currentUserId)
            parameter("pageNumber", pageNumber)
            parameter("pageSize", pageSize)
            headers {
                append("Authorization", "Bearer $token")
            }
        }
        return response.body<PostsResponse>()
    }
    suspend fun deletePost(
        postId: Long,
        token: String
    ): PostResponse {
        val response = client.delete{
            endPoint(path = "/post/$postId")
            headers {
                append("Authorization", "Bearer $token")
            }
        }
        return response.body<PostResponse>()
    }

    suspend fun connectToSocket(
        currentUserId: Long,
        token: String
    ): ResponseResource<String> = try {
        Log.d("ChatService", "connectToSocketSuccess: $currentUserId")
        webSocket = client.webSocketSession {
            webSocketEndPoint(path = "/ws/posts")
//            parameter("currentUserId", currentUserId) TODO will send this also for some personalization in future
            header(
                "Authorization",
                "Bearer $token"
            )
        }
        Log.d("ChatService", "connectToSocketSucc: ${webSocket?.isActive}")
        ResponseResource.success("Connected to socket")
    } catch (e: Exception) {
        e.printStackTrace()
        Log.d("ChatService", "connectToSocketErr: ${e.message}")
        ResponseResource.error(e.localizedMessage ?: "Unknown error")
    }

     fun receiveMessage(): Flow<String> = try {
        webSocket?.let {
            flow {
                while (true) {
                    val message = it.incoming.receive() as Frame.Text
                    emit(message.readText())
                }
            }
        } ?: emptyFlow()
    } catch (e: Exception) {
        emptyFlow()
    }


}