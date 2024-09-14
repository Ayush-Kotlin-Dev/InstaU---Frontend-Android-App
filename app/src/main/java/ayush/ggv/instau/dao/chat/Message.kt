package ayush.ggv.instau.dao.chat

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val sessionId: Long?,
    val receiver: Long?,
    val sender: Long?,
    val textMessage: String?,
    val timestamp: String?,
    val formattedTime: String?,
    val formattedDate: String?
)
