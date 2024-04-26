package ayush.ggv.instau.presentation.account.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.common.fakedata.Profile
import ayush.ggv.instau.common.fakedata.sampleProfiles
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EditProfileViewModel(

) :ViewModel() {

    var uiState by mutableStateOf(EditProfileUiState())
        private set

    fun fetchProfile(userId : Int){
        viewModelScope.launch {
            uiState = uiState.copy(
                isLoading = true
            )
            delay(1000)

             uiState = uiState.copy(
                profile = sampleProfiles.find {
                    it.id == userId
                },
                isLoading = false
            )
        }
    }

    fun updateProfile(profile: Profile){

        viewModelScope.launch {
            uiState = uiState.copy(
                isLoading = true
            )
            delay(1000)
            uiState = uiState.copy(
                isLoading = false,
                uploadSuccess = true
            )


        }
    }
}

data class EditProfileUiState(
    val isLoading: Boolean = true,
    val profile: Profile? = null,
    val uploadSuccess : Boolean = false,
    val errorMessage: String? = null
)
