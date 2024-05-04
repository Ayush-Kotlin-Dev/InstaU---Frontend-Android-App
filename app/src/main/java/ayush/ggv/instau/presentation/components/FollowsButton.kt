package ayush.ggv.instau.presentation.components


import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ayush.ggv.instau.R
import ayush.ggv.instau.ui.theme.SocialAppTheme

@Composable
fun FollowsButton(
    modifier : Modifier = Modifier,
    @StringRes text: Int,
    onFollowButtonClick: () -> Unit,
    isOutline : Boolean = false
) {
    Button(
        onClick = { onFollowButtonClick() },
        modifier = modifier,
        colors = if(isOutline) {
            ButtonDefaults.outlinedButtonColors()
        } else {
            ButtonDefaults.buttonColors()
        },
        border = if(isOutline) {
            ButtonDefaults.outlinedBorder
        } else {
            null
        },
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
        ),
        shape = MaterialTheme.shapes.medium

    ) {
        Text(
            text = stringResource(id = text),
            style = MaterialTheme.typography.button.copy(
                fontSize = 12.sp
            )
        )


    }

}
