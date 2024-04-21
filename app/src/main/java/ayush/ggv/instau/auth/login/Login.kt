package ayush.ggv.instau.auth.login

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun Login(
    navigator: DestinationsNavigator
) {
    val viewModel : LoginViewModel  = koinViewModel()
    LoginScreen(
        uiState = viewModel.uiState ,
        onUsernameChange = viewModel::updateEmail,
        onEmailChange =  viewModel::updateEmail,
    ) {

    }
}