package ayush.ggv.instau.dao.post


import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ayush.ggv.instau.model.Post
import kotlinx.serialization.Serializable
@Dao
interface PostsDao{
    @Query("SELECT * FROM post")
     fun getAllPosts(): PagingSource<Int, Post>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPosts(posts: List<Post>)

    @Query("DELETE FROM post")
    suspend fun deleteAllPosts()
}