package ayush.ggv.instau.data.posts.data

import ayush.ggv.instau.data.posts.domain.repository.PostRepository
import ayush.ggv.instau.model.PostsResponse
import ayush.ggv.instau.util.Result

class PostsRepositoryImpl(private val postService: PostService) : PostRepository {
    override suspend fun getFeedPosts(
        currentUserId: Long, page: Int, limit: Int , token : String
    ): Result<PostsResponse> {
        return try {
            val response = postService.getFeedPosts(currentUserId, page, limit ,  token)
            if (response.success ==  true) {
                Result.Success(response)
            } else {
                Result.Error(Exception("Error: ${response.message}").toString())
            }
        } catch (e: Exception) {
            Result.Error(e.toString())
        }
    }
}