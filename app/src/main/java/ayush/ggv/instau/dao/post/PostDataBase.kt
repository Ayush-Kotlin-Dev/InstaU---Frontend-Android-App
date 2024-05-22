package ayush.ggv.instau.dao.post

import androidx.room.Database
import androidx.room.RoomDatabase
import ayush.ggv.instau.model.Post

@Database(entities = [Post::class, PostRemoteKeys::class], version = 2, exportSchema = false)
abstract class PostsDatabase : RoomDatabase() {
    abstract fun postsDao(): PostsDao
    abstract fun postRemoteKeysDao(): PostRemoteKeysDao
}