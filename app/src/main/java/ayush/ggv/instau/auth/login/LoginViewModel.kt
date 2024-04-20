package ayush.ggv.instau.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel


class LoginViewModel  : ViewModel(

){
    var uiState by mutableStateOf(LoginState())
        private set

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
)