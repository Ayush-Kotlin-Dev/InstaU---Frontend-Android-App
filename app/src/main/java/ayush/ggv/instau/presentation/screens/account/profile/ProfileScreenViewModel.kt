package ayush.ggv.instau.presentation.screens.account.profile

import android.media.session.MediaSession.Token
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.common.datastore.UserSettings
import ayush.ggv.instau.common.datastore.toAuthResultData
import ayush.ggv.instau.common.fakedata.samplePosts
import ayush.ggv.instau.common.fakedata.sampleProfiles
import ayush.ggv.instau.data.profile.domain.model.Profile
import ayush.ggv.instau.domain.usecases.profileusecase.ProfileUseCase
import ayush.ggv.instau.model.Post
import ayush.ggv.instau.presentation.screens.home.HomeScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ayush.ggv.instau.util.Result
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.koinViewModel

class ProfileScreenViewModel(
    private val profileUseCase: ProfileUseCase,


) :ViewModel() {

    var userInfoUiState by mutableStateOf(UserInfoUiState())
        private set
    var profilePostUiState by mutableStateOf(ProfilePostUiState())
        private set
    fun fetchProfile(userId: Long , currentUserId : Long , token: String ) {
        userInfoUiState = userInfoUiState.copy(
            isLoading = true
        )

        viewModelScope.launch {

            try {
                val result = profileUseCase(userId, currentUserId, token)
                when (result) {
                    is Result.Success -> {
                        userInfoUiState = userInfoUiState.copy(
                            profile = result.data?.profile,
                            isLoading = false
                        )
                    }

                    is Result.Error -> {
                        userInfoUiState = userInfoUiState.copy(
                            errorMessage = result.message,
                            isLoading = false
                        )
                    }

                    is Result.Loading ->  {}
                }
            } catch (e: Exception) {
                userInfoUiState = userInfoUiState.copy(
                    errorMessage = e.message,
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
