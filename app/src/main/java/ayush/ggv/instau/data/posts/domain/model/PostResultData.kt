package ayush.ggv.instau.data.posts.domain.model

import kotlinx.serialization.Serializable

@Serializable()
data class PostResultData(
    val id: Long,
    val userId: Long,
    val userName: String,
    val userAvatar: String,
    val imageUrl: String,
    val caption: String,
    val likes: Int,
    val comments: Int,
    val isLiked: Boolean
)