package ayush.ggv.instau.presentation.screens.auth.login

import androidx.compose.runtime.Composable
import ayush.ggv.instau.presentation.screens.destinations.HomeDestination
import ayush.ggv.instau.presentation.screens.destinations.LoginDestination
import ayush.ggv.instau.presentation.screens.destinations.SignUpNDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun Login(
    navigator: DestinationsNavigator
) {
    val viewModel: LoginViewModel = koinViewModel()
    LoginScreen(
        uiState = viewModel.uiState,
        onEmailChange = viewModel::updateEmail,
        onPasswordChange = viewModel::updatePassword,
        onNavigateToHome = {
            navigator.navigate(HomeDestination) {
                popUpTo(LoginDestination.route) { inclusive = true }
            }
        },
        onSignInClick = viewModel::signIn,
        onNavigateToSignUp = {
            navigator.navigate(SignUpNDestination){
                popUpTo(LoginDestination.route) { inclusive = true }
            }
        }

    )
}