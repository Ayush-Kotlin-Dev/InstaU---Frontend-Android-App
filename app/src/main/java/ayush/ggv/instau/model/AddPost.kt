package ayush.ggv.instau.model

import kotlinx.serialization.Serializable

@Serializable
data class PostTextParams(
    val caption: String,
    val userId: Long,
    val imageUrl: String
)