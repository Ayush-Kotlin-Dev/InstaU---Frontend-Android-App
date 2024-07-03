package ayush.ggv.instau.model

import kotlinx.serialization.Serializable

@Serializable
data class PostParams(
    val caption: String,
    val userId: Long ?= null,
)