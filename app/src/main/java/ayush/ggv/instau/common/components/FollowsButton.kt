package ayush.ggv.instau.common.components


import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FollowsButton(
    modifier : Modifier = Modifier,
    @StringRes text: Int,
    onFollowButtonClick: (Boolean) -> Unit,
    isOutline : Boolean = false
) {
    Button(
        onClick = { onFollowButtonClick(isOutline) },
        modifier = modifier,
        colors = if(isOutline) {
            ButtonDefaults.buttonColors()
        } else {
            ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
        },
        border = if(isOutline) {
            ButtonDefaults.outlinedBorder
        } else {
            null
        },
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        )

    ) {
        Text(
            text = stringResource(id = text),
            style = MaterialTheme.typography.button.copy(
                fontSize = 12.sp
            )
        )


    }

}