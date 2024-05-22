package ayush.ggv.instau.dao.post
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "post_remote_keys")
data class PostRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val postId: Long,
    val prevPage: Int?,
    val nextPage: Int?,
    val lastUpdated: Long
)