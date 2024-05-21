package ayush.ggv.instau.data.posts.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import ayush.ggv.instau.dao.post.PostEntity
import ayush.ggv.instau.dao.post.PostsDatabase
import ayush.ggv.instau.dao.post.PostsRemoteMediator
import ayush.ggv.instau.data.posts.domain.repository.PostRepository
import ayush.ggv.instau.model.Post
import ayush.ggv.instau.model.PostResponse
import ayush.ggv.instau.model.PostTextParams
import ayush.ggv.instau.model.PostsResponse
import ayush.ggv.instau.util.Result
import kotlinx.coroutines.flow.Flow

class PostsRepositoryImpl(
    private val postService: PostService,
    private val database: PostsDatabase
) : PostRepository {
    override suspend fun getFeedPosts(
        currentUserId: Long, page: Int, limit: Int, token: String
    ): Result<PostsResponse> {
        return try {
            val response = postService.getFeedPosts(
                currentUserId,
                page,
                limit,
                token
            )
            if (response.success) {
                Result.Success(response)
            } else {
                Result.Error(Exception("Error: ${response.message}").toString())
            }
        } catch (e: Exception) {
            Result.Error(e.toString())
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPostsStream(pagerConfig: PagingConfig): Flow<PagingData<Post>> {
        return Pager(
            config = pagerConfig,
            pagingSourceFactory = { database.postsDao().getAllPosts() },
            remoteMediator = PostsRemoteMediator(postService, database)
        ).flow
    }

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