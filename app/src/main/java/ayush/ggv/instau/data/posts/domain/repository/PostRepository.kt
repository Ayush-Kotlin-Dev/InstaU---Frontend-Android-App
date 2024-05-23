package ayush.ggv.instau.data.posts.domain.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import ayush.ggv.instau.data.posts.domain.model.PostResultData
import ayush.ggv.instau.model.Post
import ayush.ggv.instau.model.PostResponse
import ayush.ggv.instau.model.PostTextParams
import ayush.ggv.instau.model.PostsResponse
import ayush.ggv.instau.util.Result
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    suspend fun createPost(postTextParams: PostTextParams, token: String) : Result<PostResponse>
    // In PostRepository.kt
    suspend fun getPost(postId: Long, currentUserId: Long? , token : String): Result<PostResponse>

    suspend fun getPostByUser(
        userId: Long, currentUserId: Long, pageNumber: Int, pageSize: Int , token : String
    ): Result<PostsResponse>

    suspend fun  deletePost(postId: Long , token : String) : Result<PostResponse>

    fun getPostsStream(pagerConfig: PagingConfig = PagingConfig(pageSize = 4 , prefetchDistance = 5)): Flow<PagingData<Post>>



}