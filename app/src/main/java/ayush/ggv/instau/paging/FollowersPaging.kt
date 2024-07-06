package ayush.ggv.instau.paging

import android.util.Log
import ayush.ggv.instau.data.followunfollow.domain.FollowRepository
import androidx.paging.PagingSource
import androidx.paging.PagingState
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.FollowUserData

enum class ListType {
    FOLLOWERS,
    FOLLOWING
}

class FollowPagingSource(
    private val repository: FollowRepository,
    private val userId: Long,
    private val listType: ListType
) : PagingSource<Int, FollowUserData>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FollowUserData> {
        val page = params.key ?: 1
        return try {
            val response = when (listType) {
                ListType.FOLLOWERS -> repository.getFollowers(userId, page, params.loadSize)
                ListType.FOLLOWING -> repository.getFollowing(userId, page, params.loadSize)
            }
            Log.d("FollowPagingSource", "load: $response for page: $page")

            when (response) {
                is Result.Success -> {
                    val data = response.data?.follows ?: emptyList()
                    // If the fetched data size is less than the load size, it's the end of the pagination.
                    val nextKey = if (data.size < params.loadSize) null else page + 1

                    LoadResult.Page(
                        data = data,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = nextKey
                    )
                }
                is Result.Error -> {
                    Log.e("FollowPagingSource", "Error fetching data: ${response.message}")
                    LoadResult.Error(Throwable(response.message))
                }
                else -> {
                    LoadResult.Error(Throwable("Unexpected result type"))
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, FollowUserData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}