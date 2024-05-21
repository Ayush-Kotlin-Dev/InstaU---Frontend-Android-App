package ayush.ggv.instau.dao.post

import androidx.room.Entity
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "post")
data class PostEntity (
    @androidx.room.PrimaryKey(autoGenerate = false)
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