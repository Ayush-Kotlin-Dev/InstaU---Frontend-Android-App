package ayush.ggv.instau.presentation.screens.account.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val followsUseCase: FollowsUseCase
) : ViewModel() {

    var isFollowing by mutableStateOf(false)
    var userInfoUiState by mutableStateOf(UserInfoUiState())
        private set
    var profilePostUiState by mutableStateOf(ProfilePostUiState())
        private set

    fun fetchProfile(userId: Long, currentUserId: Long, token: String) {
        userInfoUiState = userInfoUiState.copy(
            isLoading = true
        )

        viewModelScope.launch {

            try {
                val Profileresult = profileUseCase(userId, currentUserId, token)
                val postResult = getPostsbyUserIdUseCase(userId, currentUserId, 1, 10, token)

                when (Profileresult) {
                    is Result.Success -> {
                        userInfoUiState = userInfoUiState.copy(
                            profile = Profileresult.data?.profile,
                            isLoading = false
                        )
                        isFollowing = Profileresult.data?.profile?.isFollowing ?: false
                    }

                    is Result.Error -> {
                        userInfoUiState = userInfoUiState.copy(
                            errorMessage = Profileresult.message,
                            isLoading = false
                        )
                    }

                    is Result.Loading -> {}
                }

                when (postResult) {
                    is Result.Success -> {
                        profilePostUiState = profilePostUiState.copy(
                            posts = postResult.data?.posts ?: listOf(),
                            isLoading = false
                        )
                    }

                    is Result.Error -> {
                        profilePostUiState = profilePostUiState.copy(
                            errorMessage = postResult.message,
                            isLoading = false
                        )
                    }

                    is Result.Loading -> {
                        profilePostUiState = profilePostUiState.copy(isLoading = true)
                    }
                }
            } catch (e: Exception) {
                userInfoUiState = userInfoUiState.copy(
                    errorMessage = e.message,
                )
                profilePostUiState = profilePostUiState.copy(
                    errorMessage = e.message,
                )
            } finally {
                userInfoUiState = userInfoUiState.copy(
                    isLoading = false
                )
                profilePostUiState = profilePostUiState.copy(
                    isLoading = false
                )
            }
        }
    }
    fun followUnfollowUser(followsParams: FollowsParams, token: String) {
        viewModelScope.launch {
            try {
                val followResult = followsUseCase(followsParams, token)
                when (followResult) {
                    is Result.Success -> {
                        userInfoUiState = userInfoUiState.copy(
                            isLoading = false
                        )
                        isFollowing = !followsParams.isFollowing

                        userInfoUiState.profile?.let { profile ->
                            val updatedFollowersCount = if (isFollowing) profile.followersCount + 1 else profile.followersCount - 1
                            val updatedProfile = profile.copy(followersCount = updatedFollowersCount)
                            userInfoUiState = userInfoUiState.copy(profile = updatedProfile)
                        }

                    }

                    is Result.Error -> {
                        userInfoUiState = userInfoUiState.copy(
                            errorMessage = followResult.message,
                            isLoading = false
                        )
                    }

                    is Result.Loading -> {
                        userInfoUiState = userInfoUiState.copy(isLoading = true)
                    }
                }
            } catch (e: Exception) {
                userInfoUiState = userInfoUiState.copy(
                )
            } finally {
                userInfoUiState = userInfoUiState.copy(
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
    val errorMessage: String? = null
)
