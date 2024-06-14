package ayush.ggv.instau.model.chatRoom


import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomResponseDto(
    val data: List<ChatRoomData?>? = null,
    val error: Error? = null
) {
    @Serializable
    data class ChatRoomData(
        val textMessage: String?,
        val sender: Long?,
        val receiver: Long?,
        val timestamp: String?
    )

    @Serializable
    data class Error(
        val message: String? = null
    )
}