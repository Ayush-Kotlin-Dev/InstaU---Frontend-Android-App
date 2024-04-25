package ayush.ggv.instau.presentation.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.common.datastore.UserSettings
import ayush.ggv.instau.common.datastore.toUserSettings
import ayush.ggv.instau.data.auth.domain.usecases.signinusecase.SignInuseCase
import ayush.ggv.instau.data.auth.domain.usecases.signupusecases.SignUpUseCase
import kotlinx.coroutines.launch
import ayush.ggv.instau.util.Result


class LoginViewModel(
    private val signInuseCase: SignInuseCase,
    private val dataStore: DataStore<UserSettings>
) : ViewModel(

) {
    var uiState by mutableStateOf(LoginState())
        private set

    fun signIn() {
        viewModelScope.launch {
            uiState = uiState.copy(isAuthenticating = true)

            val authResultData = signInuseCase(uiState.email, uiState.password)

            uiState = when (authResultData) {
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

    fun updateEmail(email: String) {
        uiState = uiState.copy(email = email)
    }

    fun updatePassword(password: String) {
        uiState = uiState.copy(password = password)
    }

}

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isAuthenticating: Boolean = false,
    val authErrorMessage: String? = null,
    val authenticationSucceed: Boolean = false

)