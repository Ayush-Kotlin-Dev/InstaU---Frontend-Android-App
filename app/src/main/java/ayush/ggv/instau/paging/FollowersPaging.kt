package ayush.ggv.instau.paging

import android.util.Log
import ayush.ggv.instau.data.followunfollow.domain.FollowRepository


import androidx.paging.PagingSource
import androidx.paging.PagingState
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.FollowUserData

class FollowersPagingSource(
    private val repository: FollowRepository,
    private val userId: Long,
    private val token: String
) : PagingSource<Int, FollowUserData>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FollowUserData> {
        val page = params.key ?: 1
        return try {
            val response = repository.getFollowers(userId, page, params.loadSize, token)
            Log.d("FollowersPagingSource", "load: $response for page: $page")
            when (response) {
                is Result.Success -> {
                    Log.d("FollowersPagingSource", "Fetched data: ${response.data?.follows}")
                    LoadResult.Page(
                        data = response.data?.follows ?: emptyList(),
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (response.data?.follows.isNullOrEmpty()) null else page + 1
                    )
                }
                is Result.Error -> {
                    Log.e("FollowersPagingSource", "Error fetching data: ${response.message}")
                    LoadResult.Error(Throwable(response.message))
                }
                else -> {
                    LoadResult.Error(Throwable("Unexpected result type"))
                }
            }
        } catch (e: Exception) {
            Log.e("FollowersPagingSource", "Exception: $e")
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
