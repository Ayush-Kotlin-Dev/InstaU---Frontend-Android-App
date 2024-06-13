package ayush.ggv.instau.model.friendList

data class FriendList(
    val friendInfo: List<FriendInfo>? = null,
    val errorMessage: String? = null
) {
    data class FriendInfo(
        val email: String,
        val username: String,
        val avatar: String,
        val lastMessage: LastMessage? = null
    ) {
        data class LastMessage(
            val textMessage: String?,
            val timestamp: Long?
        )
    }
}
