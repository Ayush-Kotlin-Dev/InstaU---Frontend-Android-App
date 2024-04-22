package ayush.ggv.instau.auth.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.common.datastore.UserSettings
import ayush.ggv.instau.common.datastore.toUserSettings
import ayush.ggv.instau.data.auth.domain.usecases.signupusecases.SignUpUseCase
import kotlinx.coroutines.launch
import ayush.ggv.instau.util.Result
class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val dataStore: DataStore<UserSettings>
) : ViewModel() {

    var uiState by mutableStateOf(SignUpState())
    private set

    fun signUp(){
        viewModelScope.launch {
            uiState = uiState.copy(isAuthenticating = true)
            val authResultData = signUpUseCase(uiState.username, uiState.email, uiState.password)
            uiState = uiState.copy(isAuthenticating = false)

            uiState = when(authResultData){
                is Result.Error -> {
                    uiState.copy(
                        isAuthenticating = false,
                        authErrorMessage = authResultData.message ?: "An error occurred"
                    )
                }
                is Result.Success -> {
                    dataStore.updateData {
                        authResultData.data!!.toUserSettings()
                    }
                    uiState.copy(
                        isAuthenticating = false,
                        authenticationSucceed = true
                    )
                }

                is Result.Loading -> {
                    uiState.copy(
                        isAuthenticating = true
                    )
                }
            }
        }
    }

    fun updateUsername(username: String) {
        uiState = uiState.copy(username = username)
    }
    fun updateEmail(email: String) {
        uiState = uiState.copy(email = email)
    }
    fun updatePassword(password: String) {
        uiState = uiState.copy(password = password)
    }

}

data class SignUpState(
    val username : String = "",
    val email: String = "",
    val password: String = "",
    val isAuthenticating : Boolean = false,
    val authErrorMessage: String ? = null,
    val authenticationSucceed: Boolean = false

)