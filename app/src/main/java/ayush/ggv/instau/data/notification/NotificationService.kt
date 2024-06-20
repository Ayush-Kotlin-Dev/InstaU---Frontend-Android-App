package ayush.ggv.instau.data.notification

import ayush.ggv.instau.data.KtorApi
import ayush.ggv.instau.model.notification.SendMessageDto
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class NotificationService : KtorApi() {

    suspend fun sendMessage(
        sendMessageDto: SendMessageDto,
    ) {
        client.post {
            endPoint(path = "/send")
            setBody(sendMessageDto)
        }
    }

    suspend fun broadcastMessage(
        sendMessageDto: SendMessageDto,
    ) {
        client.post {
            endPoint(path = "/broadcast")
            setBody(sendMessageDto)
        }
    }
}