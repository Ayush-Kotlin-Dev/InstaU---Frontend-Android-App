package ayush.ggv.instau.model.friendList

data class RoomHistoryList(
    val roomData: List<Message>? = null,
    val errorMessage: String? = null
) {
    data class Message(
        val sessionId: String? = null,
        val receiver: Long?,
        val sender: Long?,
        val textMessage: String?,
        val timestamp: String?,
        val formattedTime: String?,
        val formattedDate: String?,
    )
}