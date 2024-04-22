package ayush.ggv.instau.common.components

import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import ayush.ggv.instau.R

@Composable
fun CustomTextFields(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPasswordTextField: Boolean = false,
    isSingleLine: Boolean = true,
    @StringRes hint: Int,
    leadingIcon: ImageVector? = null,
    isError: Boolean = false,  // New parameter for error state
    errorMessage: String? = null  // New parameter for error message
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.body2,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        singleLine = isSingleLine,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = if (isSystemInDarkTheme()) MaterialTheme.colors.surface else MaterialTheme.colors.onSurface.copy(
                alpha = 0.1f
            ),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        visualTransformation = if (isPasswordTextField) {
            if (isPasswordVisible)
                VisualTransformation.None
            else PasswordVisualTransformation()
        } else VisualTransformation.None,
        trailingIcon = if (isPasswordTextField) {
            {
                PasswordEyeIcon(
                    isPasswordVisible = isPasswordVisible,
                    onPasswordToggleClick = { isPasswordVisible = !isPasswordVisible }
                )
            }
        } else null,
        placeholder = {
            Text(
                text = stringResource(id = hint),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )
        },
        shape = MaterialTheme.shapes.medium,
        leadingIcon = leadingIcon?.let {  // Use the leadingIcon parameter here
            {
                Icon(
                    imageVector = it, contentDescription = null
                )
            }
        }
    )

}

@Composable
fun PasswordEyeIcon(
    modifier: Modifier = Modifier,
    isPasswordVisible: Boolean,
    onPasswordToggleClick: () -> Unit
) {


    val image = if (isPasswordVisible) {
        painterResource(id = R.drawable.show_eye_icon_filled)

    } else {
        painterResource(id = R.drawable.hide_eye_icon_filled)

    }
    IconButton(onClick = { onPasswordToggleClick() }) {
        Icon(
            painter = image,
            contentDescription = null,
            tint = MaterialTheme.colors.onSurface
        )

    }


}

@Preview(showBackground = true)
@Composable
fun PreviewCustomTextFields() {
    CustomTextFields(
        value = "",
        onValueChange = {},
        hint = androidx.compose.ui.R.string.default_error_message,
        keyboardType = KeyboardType.Email
    )
}