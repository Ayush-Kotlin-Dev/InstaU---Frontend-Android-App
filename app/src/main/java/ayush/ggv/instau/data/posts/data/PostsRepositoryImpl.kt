package ayush.ggv.instau.data.posts.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import ayush.ggv.instau.dao.post.PostsDatabase
import ayush.ggv.instau.dao.post.PostsRemoteMediator
import ayush.ggv.instau.data.posts.domain.repository.PostRepository
import ayush.ggv.instau.model.Post
import ayush.ggv.instau.model.PostResponse
import ayush.ggv.instau.model.PostTextParams
import ayush.ggv.instau.model.PostsResponse
import ayush.ggv.instau.util.ResponseResource
import ayush.ggv.instau.util.Result
import kotlinx.coroutines.flow.Flow

class PostsRepositoryImpl(
    private val postService: PostService,
    private val database: PostsDatabase
) : PostRepository {


    @OptIn(ExperimentalPagingApi::class)
    override fun getPostsStream(pagerConfig: PagingConfig , userId: Long, token: String): Flow<PagingData<Post>> {
        val pagingSourceFactory = { database.postsDao().getAllPosts() }
        return Pager(
            config =  PagingConfig(pageSize = 6,  prefetchDistance = 1  , initialLoadSize = 10),
            pagingSourceFactory =pagingSourceFactory,
            remoteMediator = PostsRemoteMediator(postService, database , userId , token)
        ).flow
    }

    override suspend fun connectToSocket(sender: Long, token: String): ResponseResource<String> {
        return when (val response =
            postService.connectToSocket(sender,token)) {
            is ResponseResource.Error -> ResponseResource.error(response.errorMessage)
            is ResponseResource.Success -> ResponseResource.success(response.data)
        }
    }

    override fun receiveMessage(): Flow<String> = postService.receiveMessage()

    override suspend fun createPost(
        postTextParams: PostTextParams,
        token: String
    ): Result<PostResponse> {
        return try {
            val response = postService.createPost(postTextParams, token)
            if (response.success) {
                Result.Success(response)
            } else {
                Result.Error(Exception("Error: ${response.message}").toString())
            }
        } catch (e: Exception) {
            Result.Error(e.toString())
        }
    }

    // In PostsRepositoryImpl.kt
    override suspend fun getPost(
        postId: Long,
        currentUserId: Long?,
        token: String
    ): Result<PostResponse> {
        return try {
            val response = postService.getPost(postId, currentUserId, token)
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
        currentUserId: Long,
        pageNumber: Int,
        pageSize: Int,
        token: String
    ): Result<PostsResponse> {

        return try {
            val response =
                postService.getPostsByUser(userId, currentUserId, pageNumber, pageSize, token)
            if (response.success) {
                Result.Success(response)
            } else {
                Result.Error(Exception("Error: ${response.message}").toString())
            }
        } catch (e: Exception) {
            Result.Error(e.toString())
        }

    }

    override suspend fun deletePost(postId: Long, token: String): Result<PostResponse> {
        return try {
            val response = postService.deletePost(postId, token)
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