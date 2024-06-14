package ayush.ggv.instau.model

import kotlinx.serialization.Serializable

@Serializable
data class FriendList(
    val friendInfo: List<FriendInfo>? = null,
    val errorMessage: String? = null
) {
    @Serializable
    data class FriendInfo(
        val friendId : Long,
        val username: String,
        val avatar: String,
        val lastMessage: LastMessage? = null
    ) {
        @Serializable
        data class LastMessage(
            val textMessage: String?,
            val timestamp: String?
        )
    }
}

@Serializable
data class FriendListResponseDto(
    val data: List<FriendInfoWrapper>? = null,
    val error: Error? = null
) {
    @Serializable
    data class FriendInfoWrapper(
        val friendInfo: FriendInfo?
    )

    @Serializable
    data class FriendInfo(
        val username: String,
        val userId: Long,
        val email: String,
        val avatar: String? = null,
        val lastMessage: LastMessage? = null
    ) {
        @Serializable
        data class LastMessage(
            val messageId: Long,
            val sessionId: Long,
            val textMessage: String,
            val sender: Long,
            val receiver: Long,
            val timestamp: String
        )
    }

    @Serializable
    data class Error(
        val message: String? = null
    )
}
