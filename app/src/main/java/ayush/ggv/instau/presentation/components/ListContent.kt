package ayush.ggv.instau.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

import ayush.ggv.instau.R
import ayush.ggv.instau.model.Post
import ayush.ggv.instau.presentation.screens.search.UsersUiState
import ayush.ggv.instau.ui.theme.HERO_ITEM_HEIGHT
import ayush.ggv.instau.ui.theme.LARGE_PADDING
import ayush.ggv.instau.ui.theme.MEDIUM_PADDING
import ayush.ggv.instau.ui.theme.MediumSpacing
import ayush.ggv.instau.ui.theme.SMALL_PADDING
import coil.compose.AsyncImage
import instaU.ayush.com.model.FollowUserData


@Composable
fun ListContent(
    users: UsersUiState,
    onItemClick : (Long) -> Unit ,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(top = 0.dp, start = 2.dp, end = 2.dp, bottom = 2.dp),
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        items(users.users, key = { user -> user.id }) { user ->
            HeroItem(user = user , onItemClick)
        }
    }
}

@Composable
fun HeroItem(
    user: FollowUserData,
    onItemClick: (Long) -> Unit
) {
    val topBarContentColor = if (isSystemInDarkTheme()) Color.LightGray else Color.White

    Box(
        modifier = Modifier
            .height(HERO_ITEM_HEIGHT)
            .padding(SMALL_PADDING)
            .clickable {
                onItemClick(user.id)
            },
        contentAlignment = Alignment.BottomStart
    ) {
        Surface(
            shape = RoundedCornerShape(
                size = LARGE_PADDING
            )
        ) {
            AsyncImage(
                model = user.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(ratio = 1.0f),
                contentScale = ContentScale.Crop,
                placeholder = if (MaterialTheme.colors.isLight) {
                    painterResource(id = R.drawable.light_image_place_holder)
                } else {
                    painterResource(id = R.drawable.dark_image_place_holder)
                }
            )
        }
        Surface(
            modifier = Modifier
                .fillMaxHeight(0.4f)
                .fillMaxWidth(),
            color = Color.Black.copy(alpha = ContentAlpha.medium),
            shape = RoundedCornerShape(
                bottomStart = LARGE_PADDING,
                bottomEnd = LARGE_PADDING
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MEDIUM_PADDING)
            ) {
                Text(
                    text = user.name,
                    color = topBarContentColor,
                    fontSize = MaterialTheme.typography.h5.fontSize,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = user.bio,
                    color = Color.White.copy(alpha = ContentAlpha.medium),
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}