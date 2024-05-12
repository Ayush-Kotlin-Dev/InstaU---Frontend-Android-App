package ayush.ggv.instau.presentation.screens.account.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.data.profile.domain.model.Profile
import ayush.ggv.instau.data.profile.domain.model.UpdateUserParams
import ayush.ggv.instau.domain.usecases.profileusecase.ProfileUseCase
import ayush.ggv.instau.domain.usecases.profileusecase.UpdateProfileUseCase
import ayush.ggv.instau.util.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val profileUseCase: ProfileUseCase
) :ViewModel() {
    fun setLoadingState(isLoading: Boolean) {
        uiState = uiState.copy(isLoading = isLoading)
    }
    var uiState by mutableStateOf(EditProfileUiState())
        private set

    fun updateImageUrl(newImageUrl: String) {
        uiState = uiState.copy(
            profile = uiState.profile?.copy(imageUrl = newImageUrl)
        )
    }
    var bioTextFieldValue: TextFieldValue by mutableStateOf(TextFieldValue())
        private set

    fun fetchProfile(userId: Long, currentUserId: Long, token: String) {
        viewModelScope.launch {
            uiState = uiState.copy(
                isLoading = true
            )
            val Profileresult = profileUseCase(userId, currentUserId, token)

            when (Profileresult) {
                is Result.Success -> {
                    uiState = uiState.copy(
                        profile = Profileresult.data?.profile,
                        isLoading = false
                    )
                }

                is Result.Error -> {
                    uiState = uiState.copy(
                        errorMessage = Profileresult.message,
                        isLoading = false
                    )
                }

                is Result.Loading -> {}
            }
            bioTextFieldValue = bioTextFieldValue.copy(
                text = uiState.profile?.bio ?: "",
                selection = TextRange(index = uiState.profile?.bio?.length ?: 0)
            )

        }
    }

    fun updateProfile(  token: String) {

        viewModelScope.launch {
            uiState = uiState.copy(
                isLoading = true
            )
            val result = updateProfileUseCase(
                UpdateUserParams(
                    userId = uiState.profile?.id ?: 0,
                    name = uiState.profile?.name ?: "",
                    bio = bioTextFieldValue.text,
                    imageUrl = uiState.profile?.imageUrl
                ),
                token = token
            )

            when (result) {
                is Result.Success -> {
                    uiState = uiState.copy(
                        uploadSuccess = true,
                        isLoading = false
                    )
                }

                is Result.Error -> {
                    uiState = uiState.copy(
                        errorMessage = result.message,
                        isLoading = false
                    )
                }

                is Result.Loading -> {}
            }
        }
    }

    fun onNameChange(inputName: String) {
        uiState = uiState.copy(
            profile = uiState.profile?.copy(
                name = inputName
            )
        )

    }

    fun onBioChange(inputBio: TextFieldValue) {
        bioTextFieldValue = bioTextFieldValue.copy(
            text = inputBio.text,
            selection = TextRange(inputBio.text.length)
        )
    }
}

    data class EditProfileUiState(
        val isLoading: Boolean = true,
        val profile: Profile? = null,
        val uploadSuccess: Boolean = false,
        val errorMessage: String? = null
    )

