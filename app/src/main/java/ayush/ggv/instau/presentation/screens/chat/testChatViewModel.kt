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
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestChatViewModel : ViewModel() {
    private val client = HttpClient { install(WebSockets) }
    var webSocketSession: DefaultClientWebSocketSession? = null
    val messages = mutableStateListOf<String>()

    init {
        viewModelScope.launch {
            try {
                client.webSocket(
                    method = HttpMethod.Get,
                    host = "instau-backend-server.onrender.com",
                    port = 80,
                    path = "/chat"
                ) {
                    webSocketSession = this
                    listenForMessages()
                }
            } catch (e: Exception) {
                println("WebSocket connection failed: ${e.localizedMessage}")
            }
        }
    }

    private suspend fun DefaultClientWebSocketSession.listenForMessages() {
        try {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val receivedText = frame.readText()
                    withContext(Dispatchers.Main) {
                        messages.add(receivedText)
                    }
                }
            }
        } catch (e: Exception) {
            println("Error occurred while receiving messages: ${e.localizedMessage}")
        }
    }

    fun sendInput(message: String) {
        viewModelScope.launch {
            try {
                if (message == "exit") {
                    webSocketSession?.close()
                    println("WebSocket connection closed.")
                    return@launch
                }

                webSocketSession?.send(Frame.Text(message))
                println("Message sent: $message")

            } catch (e: Exception) {
                println("Error occurred while sending message: ${e.localizedMessage}")
            }
        }
    }
}
