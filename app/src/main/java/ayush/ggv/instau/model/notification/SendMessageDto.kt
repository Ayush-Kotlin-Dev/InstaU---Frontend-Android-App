package ayush.ggv.instau.model.notification

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer


@Serializable
data class SendMessageDto(
    val to: String?,
    val notification: NotificationBody,
)
@Serializable
data class NotificationBody(
    val title: String,
    val body: String,
)