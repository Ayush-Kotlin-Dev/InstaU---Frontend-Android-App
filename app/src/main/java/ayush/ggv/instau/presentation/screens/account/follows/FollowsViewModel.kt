package ayush.ggv.instau.presentation.screens.account.follows

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.domain.usecases.followsusecase.GetFollowersUseCase
import ayush.ggv.instau.domain.usecases.followsusecase.GetFollowingUseCase
import ayush.ggv.instau.model.Post
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.FollowUserData

class FollowsViewModel(
    private val followersUseCase: GetFollowersUseCase,
     private val followingUseCase: GetFollowingUseCase
) : ViewModel(){

    var uiState by mutableStateOf(FollowsUiState())
        private set

    fun fetchFollows(userId : Long , page : Int , limit : Int , token : String){
        viewModelScope.launch {
            uiState = uiState.copy(
                isLoading = true
            )
            val followers = followersUseCase(userId , page , limit , token)
            val following = followingUseCase(userId , page , limit , token)
            when(followers){
                is Result.Success -> {

                    uiState = uiState.copy(
                        isLoading = false,
                        followUsers = followers.data?.follows ?: emptyList()
                    )
                }
                is Result.Error -> {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = followers.message
                    )
                    Log.d("error" , "Token : $token")
                }

                is Result.Loading -> TODO()
            }
            when(following){
                is Result.Success -> {
                    uiState = uiState.copy(
                        isLoading = false,
                        followingUsers = following.data?.follows ?: emptyList()
                    )
                }
                is Result.Error -> {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = following.message
                    )
                }

                is Result.Loading -> TODO()
            }

        }
    }
}

data class FollowsUiState(
    val isLoading: Boolean = false,
    val followUsers: List<FollowUserData> = emptyList(),
    val followingUsers : List<FollowUserData> = emptyList(),
    val errorMessage : String? = null
)