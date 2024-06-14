package ayush.ggv.instau.model.chatRoom


import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomResponseDto(
    val data: List<ChatRoomData?>? = null,
    val error: Error? = null
) {
    @Serializable
    data class ChatRoomData(
        val receiver: Long?,
        val sender: Long?,
        val textMessage: String?,
        val timestamp: Long?
    )

    @Serializable
    data class Error(
        val message: String? = null
    )
}