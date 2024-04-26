package ayush.ggv.instau.presentation.account.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.common.fakedata.Post
import ayush.ggv.instau.common.fakedata.Profile
import ayush.ggv.instau.common.fakedata.samplePosts
import ayush.ggv.instau.common.fakedata.sampleProfiles
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileScreenViewModel :ViewModel() {
    var userInfoUiState by mutableStateOf(UserInfoUiState())
        private set

    var profilePostUiState by mutableStateOf(ProfilePostUiState())
        private set


    fun fetchProfile(userId : Int){
        viewModelScope.launch {
            delay(1000)

            userInfoUiState = userInfoUiState.copy(
                profile = sampleProfiles.find {
                    it.id == userId
                }
            )

            //set profile ppst  ui state
            profilePostUiState = profilePostUiState.copy(
                posts = samplePosts,
                isLoading = false
            )
        }
    }
}

data class UserInfoUiState(
    val isLoading: Boolean = true,
    val profile: Profile? = null,
    val errorMessage: String? = null
)

data class ProfilePostUiState(
    val isLoading: Boolean = true,
    val posts: List<Post> = emptyList(),
    val errorMessage: String? = null
)