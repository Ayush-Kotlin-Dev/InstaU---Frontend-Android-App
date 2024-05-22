package ayush.ggv.instau.dao.post

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ayush.ggv.instau.data.posts.data.PostService
import ayush.ggv.instau.model.Post
import ayush.ggv.instau.model.PostsResponse

@OptIn(ExperimentalPagingApi::class)
class PostsRemoteMediator(
    private val service: PostService,
    private val database: PostsDatabase
) : RemoteMediator<Int, Post>() {

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
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    remoteKeys?.prevPage
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    remoteKeys?.nextPage?.plus(1) ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                }
            }

            Log.d("PostsRemoteMediator", "API request: $page")

            val response = service.getFeedPosts(
                currentUserId = 580654918340186112,
                page = page,
                limit = state.config.pageSize,
                token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJzb2NpYWxhcHBrdG9yIiwiaXNzIjoiYXl1c2guY29tIiwiZW1haWwiOiJpc2hhbkBnbWFpbC5jb20ifQ.hA8AKL_DPVVW-J2qovLStAly6DE1-dzQBsdl6jZu4Wc",
            )

            if(response.posts.isEmpty()) {
                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        database.postRemoteKeysDao().clearRemoteKeys()
                        database.postsDao().deleteAllPosts()
                    }

                    val prevKey = response.prevPage
                    val nextKey = response.nextPage
                    val keys = response.posts.map {
                        PostRemoteKeys(
                            postId = it.postId,
                            prevPage = prevKey,
                            nextPage = nextKey,
                            lastUpdated = System.currentTimeMillis()
                        )
                    }

                    database.postRemoteKeysDao().insertAll(keys)
                    database.postsDao().insertAllPosts(response.posts)
                }
            }
            MediatorResult.Success(endOfPaginationReached = response.nextPage == null)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}
