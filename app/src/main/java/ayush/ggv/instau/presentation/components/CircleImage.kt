package ayush.ggv.instau.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import ayush.ggv.instau.R
@Composable
fun CircleImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    onClick: () -> Unit = {},
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = modifier
            .clip(CircleShape)
            .clickable { onClick() },
        placeholder = if(MaterialTheme.colors.isLight){
            painterResource(id = R.drawable.light_image_place_holder)
        }else{
            painterResource(id = R.drawable.dark_image_place_holder)
        },
        contentScale = ContentScale.Crop,


    )

}