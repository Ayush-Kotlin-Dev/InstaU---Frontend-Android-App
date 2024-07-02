package ayush.ggv.instau.data.posts.data

import android.util.Log
import ayush.ggv.instau.data.KtorApi
import ayush.ggv.instau.model.PostParams
import ayush.ggv.instau.model.PostResponse
import ayush.ggv.instau.model.PostsResponse
import ayush.ggv.instau.util.ResponseResource
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.append
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.util.InternalAPI
import io.ktor.utils.io.streams.outputStream
import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PostService : KtorApi() {
    var postsWebSocket: DefaultWebSocketSession? = null

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
        Log.d("PostsRemoteMediator", "getFeedPosts: page $page ${response.bodyAsText()}")
        return response.body<PostsResponse>()
    }

    @OptIn(InternalAPI::class)
    suspend fun createPost(
        image: ByteArray,
        postTextParams: PostParams,
        token: String
    ): PostResponse {
        val jsonPostData = Json.encodeToString(postTextParams)
        val response = client.post {
            endPoint(path = "/post/create")
            setBody(MultiPartFormDataContent(
                formData {
                    append("post_data", jsonPostData)
                    append("image", "image.jpg", ContentType.Application.OctetStream) {
                        outputStream().write(image)
                    }
                }
            ))
            headers {
                append("Authorization", "Bearer $token")
            }
        }
        return response.body()
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
        token: String
    ): PostsResponse {
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
        val response = client.delete {
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
        postsWebSocket = client.webSocketSession {
            webSocketEndPoint(path = "/ws/posts")
//            parameter("currentUserId", currentUserId) TODO will send this also for some personalization in future
            header(
                "Authorization",
                "Bearer $token"
            )
        }
        Log.d("ChatService", "connectToSocketSucc: ${postsWebSocket?.isActive}")
        receiveMessage()
        ResponseResource.success("Connected to socket")
    } catch (e: Exception) {
        e.printStackTrace()
        Log.d("ChatService", "connectToSocketErr: ${e.message}")
        ResponseResource.error(e.localizedMessage ?: "Unknown error")
    }

    fun receiveMessage(): Flow<String> = try {
        Log.d(
            "ChatService",
            "receiveMessage: Called. WebSocket status: ${postsWebSocket?.isActive}"
        )
        postsWebSocket?.incoming
            ?.receiveAsFlow()
            ?.filter { it is Frame.Text }
            ?.map {
                val message = (it as Frame.Text).readText()
                message
            }
            ?: emptyFlow()
    } catch (e: Exception) {
        Log.e("ChatService", "Exception in receiveMessage: ${e.localizedMessage}", e)
        emptyFlow()
    }

    suspend fun disconnectSocket() {
        postsWebSocket?.close()
        println("WebSocket: CLOSED")
    }


}