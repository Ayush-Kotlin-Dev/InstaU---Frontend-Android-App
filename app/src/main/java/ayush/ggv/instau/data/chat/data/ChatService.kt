import android.util.Log
import ayush.ggv.instau.data.KtorApi
import ayush.ggv.instau.model.FriendListResponseDto
import ayush.ggv.instau.model.chatRoom.ChatRoomResponseDto
import ayush.ggv.instau.model.chatRoom.MessageResponseDto
import ayush.ggv.instau.util.ResponseResource
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json

class ChatService : KtorApi() {
    private var webSocket: WebSocketSession? = null

    suspend fun getFriendList(
        userId: Long,
        token: String
    ): ResponseResource<FriendListResponseDto> =
        try {
            val response = client.get {
                endPoint(path = "/chat/friends-list")
                parameter("sender", userId)
                headers {
                    append("Authorization", "Bearer $token")
                }
            }.body<FriendListResponseDto>()
            Log.d("ChatService", "getFriendList: ${response.data}")

            when (response.data) {
                null -> ResponseResource.error(response)
                else -> ResponseResource.success(response)
            }
        } catch (e: Exception) {
            Log.d("ChatService", "getFriendList: ${e.message}")
            ResponseResource.error(
                FriendListResponseDto(
                    error = FriendListResponseDto.Error("Oops, something bad happened :(")
                )
            )
        }

    suspend fun getRoomHistory(
        sender: Long,
        receiver: Long,
        token: String
    ): ResponseResource<ChatRoomResponseDto> =
        try {
            val response = client.get {
                endPoint(path = "/chat/chat-history")
                parameter("sender", sender)
                parameter("receiver", receiver)
                Log.d("ChatService", "getRoomHistory: $sender $receiver")
                header(
                    "Authorization",
                    "Bearer $token"
                )
            }.body<ChatRoomResponseDto>()
            Log.d("ChatService", "getRoomHistoryS: ${response.data}")

            when (response.data) {
                null -> ResponseResource.error(response)
                else -> ResponseResource.success(response)
            }
        } catch (e: Exception) {
            Log.d("ChatService", "getRoomHistoryErr: ${e.message}")
            ResponseResource.error(
                ChatRoomResponseDto
                    (error = ChatRoomResponseDto.Error("Oops, something bad happened :("))
            )
        }

    suspend fun connectToSocket(
        sender: Long,
        receiver: Long,
        token: String
    ): ResponseResource<String> = try {
        Log.d("ChatService", "connectToSocketSucc: $sender $receiver")
        webSocket = client.webSocketSession {
            webSocketEndPoint(path = "/chat/connect")
            parameter("sender", sender)
            parameter("receiver", receiver)
            header(
                "Authorization",
                "Bearer $token"
            )
        }
        Log.d("ChatService", "connectToSocketSucc: ${webSocket?.isActive}")
        if (webSocket?.isActive == true) {
            ResponseResource.success("Connected")
        } else {
            ResponseResource.error("Couldn't establish a connection.")
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Log.d("ChatService", "connectToSocketErr: ${e.message}")
        ResponseResource.error(e.localizedMessage ?: "Unknown error")
    }

    suspend fun sendMessage(message: String) {
        if (webSocket?.isActive == true) {
            try {
                Log.d("ChatService", "Sending message: $message")
                webSocket?.send(Frame.Text(message))
                Log.d("ChatService", "Message sent successfully")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("ChatService", "sendMessageErr: ${e.message}")
            }
        } else {
            Log.d("ChatService", "WebSocket is not connected")
        }
    }

    fun receiveMessage(): Flow<MessageResponseDto> = try {
        webSocket?.incoming
            ?.receiveAsFlow()
            ?.filter { it is Frame.Text }
            ?.map {
                val json = (it as? Frame.Text)?.readText().orEmpty()
                val messageResponseDto = Json.decodeFromString<MessageResponseDto>(json)
                messageResponseDto
            } ?: flow { }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyFlow()
    }
     suspend fun disconnectSocket() {
        webSocket?.close()
        println("WebSocket: CLOSED")
    }

}

