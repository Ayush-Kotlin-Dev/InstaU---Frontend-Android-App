package ayush.ggv.instau.presentation.screens.auth.login

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
import ayush.ggv.instau.domain.usecases.signinusecase.SignInuseCase
import ayush.ggv.instau.domain.usecases.signupusecases.SignUpUseCase
import kotlinx.coroutines.launch
import ayush.ggv.instau.util.Result
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.tasks.await


class LoginViewModel(
    private val signInuseCase: SignInuseCase,
    private val dataStore: DataStore<UserSettings>,
    private val profileService: ProfileService
) : ViewModel(

) {
    var uiState by mutableStateOf(LoginState())
        private set

    fun signIn() {
        viewModelScope.launch {
            uiState = uiState.copy(isAuthenticating = true)

            val authResultData = signInuseCase(uiState.email, uiState.password)
            val token = Firebase.messaging.token.await()


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