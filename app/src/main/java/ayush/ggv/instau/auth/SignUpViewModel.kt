package ayush.ggv.instau.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {

    var uiState by mutableStateOf(SignUpState())
    private set

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
)