package ayush.ggv.instau.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GenericPagingSource<T : Any>(
    private val pageFetcher: suspend (page: Int, pageSize: Int) -> List<T>,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: 1
        return try {
            val data = withContext(dispatcher) {
                pageFetcher(page, params.loadSize)
            }
            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}

object PaginationManager {
    inline fun <reified T : Any> createPagingFlow(
        crossinline fetcher: suspend (page: Int, pageSize: Int) -> List<T>,
        pageSize: Int = 10
    ): androidx.paging.Pager<Int, T> {
        return androidx.paging.Pager(
            config = androidx.paging.PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GenericPagingSource(fetcher) }
        )
    }
}