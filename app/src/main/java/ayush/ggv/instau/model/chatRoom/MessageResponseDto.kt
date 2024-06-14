package ayush.ggv.instau.model.chatRoom


import kotlinx.serialization.Serializable

@Serializable
data class MessageResponseDto(
    val messageId : Long?,
    val sessionId:Long?,
    val receiver: Long?,
    val sender: Long?,
    val textMessage: String?,
    val timestamp: String?
)