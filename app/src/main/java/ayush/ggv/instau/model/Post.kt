package ayush.ggv.instau.model

import androidx.room.Entity
import kotlinx.serialization.Serializable



@Serializable
@Entity(tableName = "post")
data class Post(
    @androidx.room.PrimaryKey(autoGenerate = false)
    val postId: Long,
    val caption: String,
    val imageUrl: String,
    val createdAt: String,
    val likesCount: Int,
    val commentsCount: Int,
    val userId: Long,
    val userName: String,
    val userImageUrl: String? = null,
    val isLiked: Boolean,
    val isOwnPost: Boolean,

)

@Serializable
data class PostResponse(
    val success: Boolean,
    val post: Post? = null,
    val message: String? = null
)

@Serializable
data class PostsResponse(
    val success: Boolean,
    val posts: List<Post> = listOf(),
    val message: String? = null,
    val prevPage: Int? = null,
    var nextPage: Int? = null,
)