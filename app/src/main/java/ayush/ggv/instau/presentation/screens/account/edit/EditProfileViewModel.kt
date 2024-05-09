package ayush.ggv.instau.presentation.screens.account.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.common.fakedata.Profile
import ayush.ggv.instau.common.fakedata.sampleProfiles
import ayush.ggv.instau.domain.usecases.profileusecase.ProfileUseCase
import ayush.ggv.instau.domain.usecases.profileusecase.UpdateProfileUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EditProfileViewModel(
    updateProfileUseCase: UpdateProfileUseCase,
    profileUseCase: ProfileUseCase

) :ViewModel() {

    var uiState by mutableStateOf(EditProfileUiState())
        private set

    var bioTextFieldValue : TextFieldValue by mutableStateOf(TextFieldValue())
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
            bioTextFieldValue = bioTextFieldValue.copy(
                text = uiState.profile?.bio ?: "",
                selection = TextRange(index = uiState.profile?.bio?.length ?: 0)
            )
        }
    }

    fun updateProfile(){

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
    fun onNameChange(inputName : String){
        uiState = uiState.copy(
            profile = uiState.profile?.copy(
                name = inputName
            )
        )

    }
    fun onBioChange(inputBio : TextFieldValue){
        bioTextFieldValue = bioTextFieldValue.copy(
            text = inputBio.text,
            selection = TextRange(inputBio.text.length)
        )
    }
}

data class EditProfileUiState(
    val isLoading: Boolean = true,
    val profile: Profile? = null,
    val uploadSuccess : Boolean = false,
    val errorMessage: String? = null
)
