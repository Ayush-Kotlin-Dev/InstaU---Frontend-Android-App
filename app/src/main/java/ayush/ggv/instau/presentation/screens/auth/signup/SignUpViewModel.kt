package ayush.ggv.instau.presentation.screens.auth.signup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.common.datastore.UserSettings
import ayush.ggv.instau.common.datastore.toUserSettings
import ayush.ggv.instau.data.profile.data.ProfileService
import ayush.ggv.instau.domain.usecases.signupusecases.SignUpUseCase
import kotlinx.coroutines.launch
import ayush.ggv.instau.util.Result
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.tasks.await

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val dataStore: DataStore<UserSettings>,
    private val profileService: ProfileService
) : ViewModel() {

    var uiState by mutableStateOf(SignUpState())
        private set

    fun signUp() {
        viewModelScope.launch {
            uiState = uiState.copy(isAuthenticating = true)
            val authResultData = signUpUseCase(uiState.username, uiState.email, uiState.password)
            val token = Firebase.messaging.token.await()
            uiState = uiState.copy(isAuthenticating = false)

            uiState = when (authResultData) {
                is Result.Error -> {
                    uiState.copy(
                        isAuthenticating = false,
                        authErrorMessage = authResultData.message ?: "An error occurred"
                    )
                }

                is Result.Success -> {
                    val storeIsSuccess =  profileService.storeToken(token = token , userId = authResultData.data!!.id)
                    if(storeIsSuccess.status == HttpStatusCode.OK){
                        Log.d("Token", "Token stored successfully $token")
                    }else{
                        Log.d("Token", "Token stored failed")
                    }
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
        validateUsername()

    }

    fun updateEmail(email: String) {
        uiState = uiState.copy(email = email)
    }

    fun updatePassword(password: String) {
        uiState = uiState.copy(password = password)

        // If the password field is empty, clear the error message
        if (password.isEmpty()) {
            uiState = uiState.copy(passwordErrorMessage = null)
        } else {
            // Otherwise, validate the password
            validatePassword()
        }
    }
    private fun validateUsername() {
        val isUsernameLowercase = uiState.username == uiState.username.lowercase()
        val isUsernameOnlyLettersOrUnderscoresOrDots = uiState.username.all { it.isLetter() || it == '_' || it == '.' }

        val errorMessage = when {
            !isUsernameLowercase -> "Username must be in lowercase"
            !isUsernameOnlyLettersOrUnderscoresOrDots -> "Username must only contain letters, underscores, or dots"
            else -> null
        }

        uiState = uiState.copy(usernameErrorMessage = errorMessage)
    }

    private fun validatePassword() {
        val hasUppercase = uiState.password.any { it.isUpperCase() }
        val hasSpecialChar = uiState.password.any { it in "!@#$%^&*()" }
        val hasNumber = uiState.password.any { it.isDigit() }
        val hasValidLength = uiState.password.length > 6

        val errorMessage = when {
            !hasUppercase -> "Password should contain at least one uppercase letter"
            !hasSpecialChar -> "Password should contain at least one special character"
            !hasNumber -> "Password should contain at least one number"
            !hasValidLength -> "Password should be greater than 6 characters"
            else -> null
        }

        uiState = uiState.copy(passwordErrorMessage = errorMessage)
    }

}

data class SignUpState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val isAuthenticating: Boolean = false,
    val authErrorMessage: String? = null,
    val authenticationSucceed: Boolean = false,
    val isFormValid: Boolean = false,
    val usernameErrorMessage: String? = null,
    val passwordErrorMessage: String? = null

)