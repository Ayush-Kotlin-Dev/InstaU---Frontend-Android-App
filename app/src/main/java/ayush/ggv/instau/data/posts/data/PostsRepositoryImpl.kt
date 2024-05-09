package ayush.ggv.instau.data.posts.data

import ayush.ggv.instau.data.posts.domain.model.PostResultData
import ayush.ggv.instau.data.posts.domain.repository.PostRepository
import ayush.ggv.instau.model.PostResponse
import ayush.ggv.instau.model.PostTextParams
import ayush.ggv.instau.model.PostsResponse
import ayush.ggv.instau.util.Result

class PostsRepositoryImpl(
    private val postService: PostService  ,
) : PostRepository {
    override suspend fun getFeedPosts(
        currentUserId: Long, page: Int, limit: Int , token : String
    ): Result<PostsResponse> {
        return try {
            val response = postService.getFeedPosts(currentUserId, page, limit ,  token)
            if (response.success) {
                Result.Success(response)
            } else {
                Result.Error(Exception("Error: ${response.message}").toString())
            }
        } catch (e: Exception) {
            Result.Error(e.toString())
        }
    }

    override suspend fun createPost(postTextParams: PostTextParams, token: String): Result<PostResponse> {
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
    override suspend fun getPost(postId: Long, currentUserId: Long? , token: String): Result<PostResponse> {
        return try {
            val response = postService.getPost(postId, currentUserId , token)
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
            val response = postService.getPostsByUser(userId, currentUserId, pageNumber, pageSize , token)
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