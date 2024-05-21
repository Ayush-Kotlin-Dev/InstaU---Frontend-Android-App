package ayush.ggv.instau.dao.post

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ayush.ggv.instau.data.posts.data.PostService
import ayush.ggv.instau.model.Post

@OptIn(ExperimentalPagingApi::class)
class PostsRemoteMediator(
    private val service: PostService,
    private val database: PostsDatabase
) : RemoteMediator<Int, Post>() {
    @OptIn(ExperimentalPagingApi::class)
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Post>): MediatorResult {
        try {
            // The index of the next page to be loaded
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> state.lastItemOrNull()?.postId ?: return MediatorResult.Success(endOfPaginationReached = true)
            }

            // Make the network request
            val response = service.getFeedPosts(
                currentUserId = 580654918340186112,
                page = loadKey?.toInt() ?: 1,
                limit = state.config.pageSize,
                token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJzb2NpYWxhcHBrdG9yIiwiaXNzIjoiYXl1c2guY29tIiwiZW1haWwiOiJyYXNoaUBnbWFpbC5jb20ifQ.tmetubJVWuUfrlnAU75nTDi4xniX62mO5y7T9H6r6KE",
            )

            // Save the results to the database
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.postsDao().deleteAllPosts()
                }
                database.postsDao().insertAllPosts(response.posts)
            }

            return MediatorResult.Success(endOfPaginationReached = response.posts.isEmpty())
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}