package ayush.ggv.instau.dao.post

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PostRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<PostRemoteKeys>)

    @Query("SELECT * FROM post_remote_keys WHERE postId = :postId")
    suspend fun remoteKeysPostId(postId: Long): PostRemoteKeys?

    @Query("DELETE FROM post_remote_keys")
    suspend fun clearRemoteKeys()

    @Query("SELECT MAX(lastUpdated) FROM post_remote_keys")
    suspend fun getLastUpdatedTimestamp(): Long?
}