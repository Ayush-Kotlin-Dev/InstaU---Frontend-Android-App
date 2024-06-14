package ayush.ggv.instau.model.friendList

import kotlinx.serialization.Serializable

@Serializable
data class RoomHistoryList(
    val roomData: List<Message>? = null,
    val errorMessage: String? = null
) {
    @Serializable
    data class Message(
        val sessionId: Long? = null,
        val receiver: Long?,
        val sender: Long?,
        val textMessage: String?,
        val timestamp: String?,
        val formattedTime: String?,
        val formattedDate: String?,
    )
}