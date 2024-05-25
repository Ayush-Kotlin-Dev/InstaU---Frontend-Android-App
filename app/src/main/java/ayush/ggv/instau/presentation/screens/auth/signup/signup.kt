package ayush.ggv.instau.presentation.screens.auth.signup

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ayush.ggv.instau.R
import ayush.ggv.instau.presentation.components.CustomTextFields
import ayush.ggv.instau.ui.theme.ButtonHeight
import ayush.ggv.instau.ui.theme.ExtraLargeSpacing
import ayush.ggv.instau.ui.theme.LargeSpacing
import ayush.ggv.instau.ui.theme.MediumSpacing
import ayush.ggv.instau.ui.theme.SmallSpacing

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    uiState: SignUpState,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    onSignupClick: () -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(
                    color = if (isSystemInDarkTheme())
                        MaterialTheme.colors.background
                    else MaterialTheme.colors.surface
                )
                .padding(
                    top = ExtraLargeSpacing + LargeSpacing,
                    start = LargeSpacing + MediumSpacing,
                    end = LargeSpacing + MediumSpacing,
                    bottom = LargeSpacing
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(LargeSpacing)
        ) {

            CustomTextFields(
                value = uiState.username,
                onValueChange = onUsernameChange,
                hint = R.string.username_hint ,
                leadingIcon = Icons.Default.AccountCircle ,
                isError = uiState.usernameErrorMessage != null,
                errorMessage = uiState.usernameErrorMessage




            )
            CustomTextFields(
                value = uiState.email,
                onValueChange = onEmailChange,
                hint = R.string.email_hint,
                keyboardType = KeyboardType.Email,
                leadingIcon = Icons.Default.Email
            )
            CustomTextFields(
                value = uiState.password,
                onValueChange = onPasswordChange,
                hint = R.string.password_hint,
                keyboardType = KeyboardType.Password,
                isPasswordTextField = true,
                leadingIcon = Icons.Default.Lock,
                isError = uiState.passwordErrorMessage != null,
                errorMessage = uiState.passwordErrorMessage
            )
            Button(
                onClick = { onSignupClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                ),
                shape = MaterialTheme.shapes.medium,
                enabled = !uiState.isAuthenticating
                        && uiState.username.isNotBlank()
                        && uiState.email.isNotBlank()
                        && uiState.password.isNotBlank()
                        && uiState.usernameErrorMessage == null
                        && uiState.passwordErrorMessage == null            ) {
                if (uiState.isAuthenticating) {
                    CircularProgressIndicator(color = MaterialTheme.colors.onPrimary)
                } else {
                    Text(text = stringResource(id = R.string.signup_button_hint))
                }
            }
            GoToLogin(modifier) {
                onNavigateToLogin()
            }


        }
    }
    LaunchedEffect (
        key1 = uiState.authenticationSucceed  ,
        key2 = uiState.authErrorMessage
    ){
        if (uiState.authenticationSucceed) {
            onNavigateToHome()
        }

        if (uiState.authErrorMessage != null) {
            Toast.makeText(context, uiState.authErrorMessage, Toast.LENGTH_SHORT).show()
        }

    }

}

@Composable
fun GoToLogin(modifier: Modifier = Modifier,
              onNavigateToLogin: () -> Unit
) {
    Row(
        modifier = modifier, horizontalArrangement = Arrangement.spacedBy(
            SmallSpacing
        )
    ) {
        Text(text = "Have already an account?", style = MaterialTheme.typography.caption)
        Text(
            text = "Login",
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.primary,
            modifier = modifier.clickable { onNavigateToLogin() }
        )
    }
}
