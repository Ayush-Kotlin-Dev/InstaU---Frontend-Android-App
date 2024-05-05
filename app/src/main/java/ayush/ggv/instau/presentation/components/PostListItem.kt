package ayush.ggv.instau.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ayush.ggv.instau.R
import ayush.ggv.instau.common.fakedata.Post
import ayush.ggv.instau.common.fakedata.samplePosts
import ayush.ggv.instau.ui.theme.DarkGray
import ayush.ggv.instau.ui.theme.ExtraLargeSpacing
import ayush.ggv.instau.ui.theme.LargeSpacing
import ayush.ggv.instau.ui.theme.LightGray
import ayush.ggv.instau.ui.theme.MediumSpacing
import ayush.ggv.instau.ui.theme.SocialAppTheme
import coil.compose.AsyncImage
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun PostListItem(
    modifier: Modifier = Modifier,
    post: Post,
    onPostClick: (Post) -> Unit,
    onProfileClick: (Int) -> Unit,
    onLikeClick: (String) -> Unit,
    onCommentClick: (String) -> Unit,
    isDetailScreen: Boolean = false


) {

    // Parse the date string into a LocalDateTime object
    val dateTime = LocalDateTime.parse(post.createdAt, DateTimeFormatter.ISO_DATE_TIME)

    // Format the LocalDateTime object into a "time ago" string
    val timeAgo = formatTimeAgo(dateTime)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.surface,
            )
            .clickable { onPostClick(post) }
            .padding(
                bottom = ExtraLargeSpacing
            )
    ) {
        PostItemHeader(
            name = post.authorName,
            profileUrl = post.authorImage,
            date = timeAgo,
            onProfileClick = {
                onProfileClick(post.authorId)
            }
        )
        AsyncImage(
            model = post.imageUrl,
            contentDescription = null,
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(ratio = 1.0f),
            contentScale = ContentScale.Crop,
            placeholder = if (MaterialTheme.colors.isLight) {
                painterResource(id = R.drawable.light_image_place_holder)
            } else {
                painterResource(id = R.drawable.dark_image_place_holder)
            }
        )
        PostLikeRow(
            onLikeClick = { onLikeClick(post.id) },
            onCommentClick = { onCommentClick(post.id) },
            likesCount = post.commentCount,
            commentsCount = post.commentCount
        )
        Text(
            text = post.text,
            style = MaterialTheme.typography.body2,
            modifier = modifier
                .padding(horizontal = LargeSpacing),
            maxLines = if (isDetailScreen) 20 else 2,
            overflow = TextOverflow.Ellipsis
        )



    }
}

@Composable
fun PostItemHeader(
    modifier: Modifier = Modifier,
    name: String,
    profileUrl: String,
    date: String,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = LargeSpacing,
                vertical = MediumSpacing
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MediumSpacing)
    ) {

        CircleImage(
            imageUrl = profileUrl,
            modifier = modifier.size(30.dp))
        {
            onProfileClick()
        }
        Text(
            text = name,
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.onSurface
        )
        Box(
            modifier = modifier
                .size(4.dp)
                .clip(CircleShape)
                .background(
                    color = if (MaterialTheme.colors.isLight) {
                        LightGray
                    } else {
                        DarkGray
                    }
                )
        )

        Text(
            text = date,
            style = MaterialTheme.typography.caption.copy(
                textAlign = TextAlign.Start,
                fontSize = 12.sp,
                color = if (MaterialTheme.colors.isLight) {
                    LightGray
                } else {
                    DarkGray
                }
            ),
            modifier = modifier.weight(1f)
        )

        Icon(
            painter = painterResource(id = R.drawable.round_more_horiz_24),
            contentDescription = null,
            tint = if (MaterialTheme.colors.isLight) {
                LightGray
            } else {
                DarkGray
            }
        )
    }
}


@Composable
fun PostLikeRow(
    modifier: Modifier = Modifier,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    likesCount: Int,
    commentsCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = MediumSpacing,
                vertical = 0.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = { onLikeClick() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.like_icon_outlined),
                contentDescription = null,
                tint = if (MaterialTheme.colors.isLight) {
                    LightGray
                } else {
                    DarkGray

                }
            )
        }
        Text(
            text = "$likesCount",
            style = MaterialTheme.typography.subtitle2.copy(fontSize = 18.sp),
        )
        Spacer(modifier = Modifier.width(MediumSpacing))

        IconButton(
            onClick = { onCommentClick() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.chat_icon_outlined),
                contentDescription = null,
                tint = if (MaterialTheme.colors.isLight) {
                    LightGray
                } else {
                    DarkGray

                }
            )
        }
        Text(
            text = "$commentsCount",
            style = MaterialTheme.typography.subtitle2.copy(fontSize = 18.sp),
        )
        Spacer(modifier = Modifier.width(MediumSpacing))
    }
}

@Preview(showBackground = false , uiMode = UI_MODE_NIGHT_YES  )
@Composable
fun PostListItemPreview() {
    SocialAppTheme {
        Surface(
            color = MaterialTheme.colors.surface
        ) {
            val context = LocalContext.current
            PostListItem(
                post = samplePosts.first(),
                onPostClick = { Toast.makeText(context, "onPostClick", Toast.LENGTH_SHORT).show() },
                onProfileClick = {
                    Toast.makeText(context, "onProfile", Toast.LENGTH_SHORT).show()
                },
                onLikeClick = { Toast.makeText(context, "onPostClick", Toast.LENGTH_SHORT).show() },
                onCommentClick = {
                    Toast.makeText(context, "onPostClick", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

fun formatTimeAgo(dateTime: LocalDateTime): String {
    val duration = Duration.between(dateTime, LocalDateTime.now())
    return when {
        duration.toDays() > 0 -> "${duration.toDays()} days ago"
        duration.toHours() > 0 -> "${duration.toHours()} hours ago"
        duration.toMinutes() > 0 -> "${duration.toMinutes()} minutes ago"
        else -> "just now"
    }
}