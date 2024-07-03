package ayush.ggv.instau.data.posts.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import ayush.ggv.instau.common.datastore.UserPreferences
import ayush.ggv.instau.dao.post.PostsDatabase
import ayush.ggv.instau.dao.post.PostsRemoteMediator
import ayush.ggv.instau.data.posts.domain.repository.PostRepository
import ayush.ggv.instau.model.Post
import ayush.ggv.instau.model.PostResponse
import ayush.ggv.instau.model.PostParams
import ayush.ggv.instau.model.PostsResponse
import ayush.ggv.instau.util.ResponseResource
import ayush.ggv.instau.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PostsRepositoryImpl(
    private val postService: PostService,
    private val database: PostsDatabase,
    private val userPreferences: UserPreferences
) : PostRepository {


    @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
    override fun getPostsStream(pagerConfig: PagingConfig): Flow<PagingData<Post>> {
        return flow {
            val userData = userPreferences.getUserData()
            emit(userData)
        }.map { userData ->
            val pagingSourceFactory = { database.postsDao().getAllPosts() }
            Pager(
                config = PagingConfig(pageSize = 6, prefetchDistance = 1, initialLoadSize = 10),
                pagingSourceFactory = pagingSourceFactory,
                remoteMediator = PostsRemoteMediator(postService, database, userData.id, userData.token)
            ).flow
        }.flattenConcat()
    }

    override suspend fun connectToSocket(): ResponseResource<String> {
        val userData = userPreferences.getUserData()

        return when (val response =
            postService.connectToSocket(userData.id, userData.token)) {
            is ResponseResource.Error -> ResponseResource.error(response.errorMessage)
            is ResponseResource.Success -> ResponseResource.success(response.data)
        }
    }

    override  fun receiveMessage(): Flow<String> = postService.receiveMessage()
    override suspend fun disconnectSocket() {
        postService.disconnectSocket()
    }

    override suspend fun createPost(
        image: ByteArray,
        postTextParams: PostParams
    ): Result<PostResponse> {
        return try {
            val userData = userPreferences.getUserData()
            val response = postService.createPost(image , postTextParams.copy(userId = userData.id),userData.token)
            if (response.success) {
                Result.Success(response)
            } else {
                Result.Error(Exception("Error: ${response.message}").toString())
            }
        } catch (e: Exception) {
            Result.Error(e.toString())
        }
    }

    override suspend fun getPost(postId: Long): Result<PostResponse> {

        return try {
            val userData = userPreferences.getUserData()

            val response = postService.getPost(postId , userData.id, userData.token)
            if (response.success) {
                Result.Success(response)
            } else {
                Result.Error(Exception("Error: ${response.message}").toString())
            }
        } catch (e: Exception) {
            Result.Error(e.toString())
        }
    }

    override suspend fun getPostByUser(
        userId: Long,
        pageNumber: Int,
        pageSize: Int,
    ): Result<PostsResponse> {
        val userData = userPreferences.getUserData()
        Log.d("PostService", "getPostsByUser: Called Repo with data $userData")
        return try {
            val response =
                postService.getPostsByUser(userId, userData.id, pageNumber, pageSize, userData.token)
            if (response.success) {
                Result.Success(response)
            } else {
                Result.Error(Exception("Error: ${response.message}").toString())
            }
        } catch (e: Exception) {
            Result.Error(e.toString())
        }

    }

    override suspend fun deletePost(postId: Long): Result<PostResponse> {
        return try {
            val userData = userPreferences.getUserData()
            val response = postService.deletePost(postId , userData.token)
            if (response.success) {
                Result.Success(response)
            } else {
                Result.Error(Exception("Error: ${response.message}").toString())
            }
        } catch (e: Exception) {
            Result.Error(e.toString())
        }
    }


}