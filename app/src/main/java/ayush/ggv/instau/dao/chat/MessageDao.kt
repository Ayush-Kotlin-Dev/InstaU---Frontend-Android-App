package ayush.ggv.instau.dao.chat

import androidx.room.*

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE (sender = :userId AND receiver = :otherUserId) OR (sender = :otherUserId AND receiver = :userId) ORDER BY timestamp DESC")
    suspend fun getMessages(userId: Long, otherUserId: Long): List<MessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Query("DELETE FROM messages WHERE (sender = :userId AND receiver = :otherUserId) OR (sender = :otherUserId AND receiver = :userId)")
    suspend fun deleteMessages(userId: Long, otherUserId: Long)
}