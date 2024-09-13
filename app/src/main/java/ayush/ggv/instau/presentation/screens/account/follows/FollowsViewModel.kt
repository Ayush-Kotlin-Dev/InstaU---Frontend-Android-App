package ayush.ggv.instau.presentation.screens.account.follows

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import ayush.ggv.instau.domain.usecases.followsusecase.GetFollowersUseCase
import ayush.ggv.instau.domain.usecases.followsusecase.GetFollowingUseCase
import ayush.ggv.instau.model.Post
import ayush.ggv.instau.paging.FollowPagingSource
import ayush.ggv.instau.paging.ListType
import ayush.ggv.instau.paging.PaginationManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.FollowUserData
import kotlinx.coroutines.flow.Flow

class FollowsViewModel(
    private val followersUseCase: GetFollowersUseCase,
    private val followingUseCase: GetFollowingUseCase
) : ViewModel() {

    var uiState by mutableStateOf(FollowsUiState())
        private set

    private fun fetchFollowers(userId: Long): Flow<PagingData<FollowUserData>> {
        return PaginationManager.createPagingFlow(
            fetcher = { page, pageSize ->
                followersUseCase.repository.getFollowers(userId, page, pageSize).data?.follows ?: emptyList()
            }
        ).flow.cachedIn(viewModelScope)
    }

    private fun fetchFollowing(userId: Long): Flow<PagingData<FollowUserData>> {
        return PaginationManager.createPagingFlow(
            fetcher = { page, pageSize ->
                followingUseCase.repository.getFollowing(userId, page, pageSize).data?.follows ?: emptyList()
            }
        ).flow.cachedIn(viewModelScope)
    }

    fun fetchFollows(userId: Long) {
        viewModelScope.launch {
            val followers = fetchFollowers(userId)
            val following = fetchFollowing(userId)
            uiState = uiState.copy(
                followUsers = followers,
                followingUsers = following
            )
        }
    }
}



data class FollowsUiState(
    val followUsers: Flow<PagingData<FollowUserData>>? = null ,
    val followingUsers :Flow<PagingData<FollowUserData>>? = null
)