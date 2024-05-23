package ayush.ggv.instau.dao.post

import android.util.Log
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
    private val database: PostsDatabase,
    private val userId: Long,
    private val token: String
) : RemoteMediator<Int, Post>() {


    private val cacheTimeout = 60// minutes

    override suspend fun initialize(): InitializeAction {
        val currentTime = System.currentTimeMillis()
        val lastUpdated = database.postRemoteKeysDao().getLastUpdatedTimestamp() ?: 0
        val diffInMinutes = (currentTime - lastUpdated) / 1000 / 60
        return if (diffInMinutes > cacheTimeout) {
            Log.d("PostsRemoteMediator" ,"Not skipping initial refresh")
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            Log.d("PostsRemoteMediator" ,"skipping initial refresh")
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Post>
    ): PostRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.postId?.let { postId ->
                database.postRemoteKeysDao().remoteKeysPostId(postId)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Post>): PostRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { post ->
            database.postRemoteKeysDao().remoteKeysPostId(post.postId)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Post>): PostRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { post ->
            database.postRemoteKeysDao().remoteKeysPostId(post.postId)
        }
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, Post>
    ): MediatorResult {
        try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextPage
                }
            }

            Log.d("PostsRemoteMediator", "API request: $page")

            val response = service.getFeedPosts(
                currentUserId = userId,
                page = page,
                limit = state.config.pageSize,
                token = token
            )

            val posts = response.posts
            val endOfPaginationReached = posts.size < state.config.pageSize

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.postRemoteKeysDao().clearRemoteKeys()
                    database.postsDao().deleteAllPosts()
                }

                val keys = posts.map {
                    PostRemoteKeys(
                        postId = it.postId,
                        prevPage = if (page == 1) null else page - 1,
                        nextPage = if (endOfPaginationReached) null else page + 1,
                        lastUpdated = System.currentTimeMillis()
                    )
                }

                database.postRemoteKeysDao().insertAll(keys)
                database.postsDao().insertAllPosts(posts)
            }

            Log.d("PostsRemoteMediator", "End of pagination reached: $endOfPaginationReached")

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            Log.e("PostsRemoteMediator", "Error: ${e.message}", e)
            return MediatorResult.Error(e)
        }
    }

}
