package ayush.ggv.instau.presentation.account.follows

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.common.fakedata.FollowsUser
import ayush.ggv.instau.common.fakedata.sampleUsers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FollowsViewModel : ViewModel(){

    var uiState by mutableStateOf(FollowsUiState())
        private set

    fun fetchFollows(userId : Int , followsType : Int){
        viewModelScope.launch {
            uiState = uiState.copy(
                isLoading = true
            )
            delay(1000)

            uiState = uiState.copy(
                isLoading = false,
                followUsers = sampleUsers
            )
        }
    }
}

data class FollowsUiState(
    val isLoading: Boolean = false,
    val followUsers: List<FollowsUser> = emptyList(),
    val errorMessage : String? = null
)