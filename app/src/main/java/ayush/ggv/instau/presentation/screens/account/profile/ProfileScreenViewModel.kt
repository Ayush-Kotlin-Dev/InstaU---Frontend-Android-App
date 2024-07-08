package ayush.ggv.instau.presentation.screens.account.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import ayush.ggv.instau.common.datastore.UserSettings
import ayush.ggv.instau.data.profile.domain.model.Profile
import ayush.ggv.instau.domain.usecases.followsusecase.FollowsUseCase
import ayush.ggv.instau.domain.usecases.postsusecase.getPostsByuserIdUseCase
import ayush.ggv.instau.domain.usecases.profileusecase.ProfileUseCase
import ayush.ggv.instau.model.Post
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.FollowsParams
import kotlinx.coroutines.launch

class ProfileScreenViewModel(
    private val profileUseCase: ProfileUseCase,
    private val getPostsbyUserIdUseCase: getPostsByuserIdUseCase,
    private val followsUseCase: FollowsUseCase,
    private val userSettingsDataStore: DataStore<UserSettings> // Inject the DataStore instance

) : ViewModel() {

    var isFollowing: Boolean = false
        private set
    private val _profilePostUiState = mutableStateOf(ProfilePostUiState())
    var profilePostUiState: State<ProfilePostUiState> = _profilePostUiState

    private val _userInfoUiState = mutableStateOf(UserInfoUiState())
    val userInfoUiState: State<UserInfoUiState> = _userInfoUiState

    var postsCount by mutableStateOf(0)
    fun logout() {
        viewModelScope.launch {
            val newSettings = UserSettings() // Create a new instance with default values
            userSettingsDataStore.updateData { newSettings }
        }
    }

    fun fetchProfile(
        userId: Long,
    ) {
        _userInfoUiState.value = _userInfoUiState.value.copy(isLoading = true)

        viewModelScope.launch {

            try {
                val profileResult = profileUseCase(userId)
                val postResult = getPostsbyUserIdUseCase(userId, 1, 10)

                when (profileResult) {
                    is Result.Success -> {
                        _userInfoUiState.value = _userInfoUiState.value.copy(
                            profile = profileResult.data?.profile,
                            isLoading = false
                        )
                        isFollowing = profileResult.data?.profile?.isFollowing ?: false
                    }

                    is Result.Error -> {
                        _userInfoUiState.value = _userInfoUiState.value.copy(
                            errorMessage = profileResult.message,
                            isLoading = false
                        )
                    }

                    is Result.Loading -> {
                        _userInfoUiState.value = _userInfoUiState.value.copy(
                            isLoading = true
                        )
                    }
                }

                when (postResult) {
                    is Result.Success -> {
                        postsCount = postResult.data?.posts?.size ?: 0
                        _profilePostUiState.value = _profilePostUiState.value.copy(
                            posts = postResult.data?.posts ?: emptyList(),
                            postsCount = postsCount,
                            isLoading = false
                        )
                    }

                    is Result.Error -> {
                        _profilePostUiState.value = _profilePostUiState.value.copy(
                            errorMessage = postResult.message,
                            isLoading = false
                        )
                    }

                    is Result.Loading -> {
                        _profilePostUiState.value = _profilePostUiState.value.copy(
                            isLoading = true
                        )
                    }
                }
            } catch (e: Exception) {
                _userInfoUiState.value = _userInfoUiState.value.copy(
                    errorMessage = e.message,
                    isLoading = false
                )
                _profilePostUiState.value = _profilePostUiState.value.copy(
                    errorMessage = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun followUnfollowUser(followsParams: FollowsParams) {
        viewModelScope.launch {
            try {
                val followResult = followsUseCase(followsParams)
                when (followResult) {
                    is Result.Success -> {
                        _userInfoUiState.value = _userInfoUiState.value.copy(
                            isLoading = false
                        )
                        isFollowing = !followsParams.isFollowing

                        _userInfoUiState.value.profile?.let { profile ->
                            val updatedFollowersCount =
                                if (isFollowing) profile.followersCount + 1 else profile.followersCount - 1
                            val updatedProfile =
                                profile.copy(followersCount = updatedFollowersCount)
                            _userInfoUiState.value = _userInfoUiState.value.copy(profile = updatedProfile)
                        }

                    }

                    is Result.Error -> {
                        _userInfoUiState.value = _userInfoUiState.value.copy(
                            errorMessage = followResult.message,
                            isLoading = false
                        )
                    }

                    is Result.Loading -> {
                        _userInfoUiState.value = _userInfoUiState.value.copy(
                            isLoading = true
                        )
                    }
                }
            } catch (e: Exception) {
                _userInfoUiState.value = _userInfoUiState.value.copy(
                    errorMessage = e.message,
                    isLoading = false
                )
            }
        }
    }
}

data class UserInfoUiState(
    var isLoading: Boolean = false,
    val profile: Profile? = null,
    val errorMessage: String? = null
)

data class ProfilePostUiState(
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    val errorMessage: String? = null,
    val postsCount: Int = 0
)
