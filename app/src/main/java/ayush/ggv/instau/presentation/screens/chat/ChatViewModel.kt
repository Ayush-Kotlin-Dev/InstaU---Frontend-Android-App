package ayush.ggv.instau.presentation.screens.chat

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel : ViewModel() {
    private val client = HttpClient { install(WebSockets) }
    var webSocketSession: DefaultClientWebSocketSession? = null
    val messages = mutableStateListOf<String>()

    init {
        viewModelScope.launch {
            client.webSocket(
                method = HttpMethod.Get,
                host = "instau-backend-server.onrender.com",
                port = 80, // Port 80 for HTTP
                path = "/chat"
            ) {
                webSocketSession = this
                launch {
                    getAndDisplayOutput { receivedMessage ->
                        messages.add(receivedMessage)
                    }
                }
            }
        }
    }


    suspend fun DefaultClientWebSocketSession.getAndDisplayOutput(onMessageReceived: (String) -> Unit) {
        try {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val receivedText = frame.readText()
                    withContext(Dispatchers.Main) {
                        onMessageReceived(receivedText)
                    }
                }
            }
        } catch (e: Exception) {
            println("Error occurred while receiving ${e.localizedMessage}")
        }
    }

    suspend fun sendInput(message: String) {
        webSocketSession?.let { session ->
            try {
                session.send(Frame.Text(message))
            } catch (e: Exception) {
                println("Error occurred while sending ${e.localizedMessage}")
            }
        }
    }
}