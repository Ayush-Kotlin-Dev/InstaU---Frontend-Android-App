package ayush.ggv.instau.auth

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ayush.ggv.instau.R
import ayush.ggv.instau.common.components.CustomTextFields
import ayush.ggv.instau.ui.theme.ButtonHeight
import ayush.ggv.instau.ui.theme.ExtraLargeSpacing
import ayush.ggv.instau.ui.theme.LargeSpacing
import ayush.ggv.instau.ui.theme.MediumSpacing

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    uiState: SignUpState,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNavigateToLogin: () -> Unit
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
            hint = R.string.username_hint
        )
        CustomTextFields(
            value = uiState.email,
            onValueChange = onEmailChange,
            hint = R.string.email_hint,
            keyboardType = KeyboardType.Email
        )
        CustomTextFields(
            value = uiState.password,
            onValueChange = onPasswordChange,
            hint = R.string.password_hint,
            keyboardType = KeyboardType.Password,
            isPasswordTextField = true
        )
        Button(
            onClick = { onNavigateToLogin() },
            modifier = Modifier
                .fillMaxWidth()
                .height(ButtonHeight),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp
            ),
            shape = MaterialTheme.shapes.medium
        ){
            Text(text = stringResource(id = R.string.signup_button_hint) )
        }


    }
}

@Composable
@Preview(showBackground = true)
fun PreviewSignUpScreen() {
    SignUpScreen(
        uiState = SignUpState(),
        onUsernameChange = {},
        onEmailChange = {},
        onPasswordChange = {},
        onNavigateToLogin = {}
    )
}