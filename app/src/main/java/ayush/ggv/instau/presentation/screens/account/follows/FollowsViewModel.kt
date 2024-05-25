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
    private fun fetchFollowers(userId: Long, token: String): Flow<PagingData<FollowUserData>> {
        Log.d("FollowsViewModel", "Creating Pager for followers")
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                FollowPagingSource(followersUseCase.repository, userId, token, listType = ListType.FOLLOWERS)
            }
        ).flow.cachedIn(viewModelScope)
    }

    private fun fetchFollowing(userId: Long, token: String): Flow<PagingData<FollowUserData>> {
        Log.d("FollowsViewModel", "Creating Pager for following")
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                FollowPagingSource(followingUseCase.repository, userId, token , listType = ListType.FOLLOWING)
            }
        ).flow.cachedIn(viewModelScope)
    }

    fun fetchFollows(userId: Long, token: String) {
        viewModelScope.launch {
            val followers = fetchFollowers(userId, token)
            val following = fetchFollowing(userId, token)
            uiState = uiState.copy(
                followUsers = followers,
                followingUsers = following
            )
            Log.d("FollowsViewModel", "Updated uiState with followers flow")
        }
    }
}



data class FollowsUiState(
    val followUsers: Flow<PagingData<FollowUserData>>? = null ,
    val followingUsers :Flow<PagingData<FollowUserData>>? = null
)