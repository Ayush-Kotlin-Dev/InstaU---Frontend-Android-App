package ayush.ggv.instau.auth

import androidx.compose.runtime.Composable
import ayush.ggv.instau.auth.destinations.LoginDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
@Destination(start = true)
fun SignUpN(
    navigator: DestinationsNavigator
) {
    val viewModel : SignUpViewModel  = koinViewModel()

    SignUpScreen(
        uiState = viewModel.uiState,
        onUsernameChange = viewModel::updateUsername,
        onEmailChange = viewModel::updateEmail,
        onPasswordChange = viewModel::updatePassword,
        onNavigateToLogin = {
            navigator.navigate(LoginDestination)
        },
    )
}