package ayush.ggv.instau.model

import kotlinx.serialization.Serializable

@Serializable
data class FriendList(
    val friendInfo: List<FriendInfo>? = null,
    val errorMessage: String? = null
) {
    @Serializable
    data class FriendInfo(
        val email: String,
        val username: String,
        val avatar: String,
        val lastMessage: LastMessage? = null
    ) {
        @Serializable
        data class LastMessage(
            val textMessage: String?,
            val timestamp: Long?
        )
    }
}

@Serializable
data class FriendListResponseDto(
    val data: List<FriendListData?>? = null,
    val error: Error? = null
) {
    @Serializable
    data class FriendListData(
        val friendInfo: FriendInfo?,
    ) {
        @Serializable
        data class FriendInfo(
            val avatar: String?,
            val email: String?,
            val lastMessage: LastMessage?,
            val username: String?
        ) {
            @Serializable
            data class LastMessage(
                val receiver: String?,
                val sender: String?,
                val textMessage: String?,
                val timestamp: Long?
            )
        }
    }

    @Serializable
    data class Error(
        val message: String? = null
    )
}