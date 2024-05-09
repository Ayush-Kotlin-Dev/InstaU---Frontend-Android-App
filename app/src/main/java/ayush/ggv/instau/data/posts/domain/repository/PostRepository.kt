package ayush.ggv.instau.data.posts.domain.repository

import ayush.ggv.instau.data.posts.domain.model.PostResultData
import ayush.ggv.instau.model.PostResponse
import ayush.ggv.instau.model.PostTextParams
import ayush.ggv.instau.model.PostsResponse
import ayush.ggv.instau.util.Result

interface PostRepository {
    suspend fun getFeedPosts(currentUserId: Long, page: Int, limit: Int ,token : String ) : Result<PostsResponse>

    suspend fun createPost(postTextParams: PostTextParams, token: String) : Result<PostResponse>
    // In PostRepository.kt
    suspend fun getPost(postId: Long, currentUserId: Long? , token : String): Result<PostResponse>
    suspend fun getPostByUser(
        userId: Long, currentUserId: Long, pageNumber: Int, pageSize: Int , token : String
    ): Result<PostsResponse>



}