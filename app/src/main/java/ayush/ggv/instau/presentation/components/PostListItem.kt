package ayush.ggv.instau.presentation.components

import android.app.DownloadManager
import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import android.os.Environment
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import ayush.ggv.instau.R
import ayush.ggv.instau.model.Post
import ayush.ggv.instau.presentation.screens.account.profile.ProfileScreenViewModel
import ayush.ggv.instau.presentation.screens.destinations.HomeDestination
import ayush.ggv.instau.presentation.screens.destinations.PostDetailDestination
import ayush.ggv.instau.presentation.screens.destinations.ProfileDestination
import ayush.ggv.instau.presentation.screens.home.HomeScreenViewModel
import ayush.ggv.instau.ui.theme.DarkGray
import ayush.ggv.instau.ui.theme.ExtraLargeSpacing
import ayush.ggv.instau.ui.theme.LargeSpacing
import ayush.ggv.instau.ui.theme.LightGray
import ayush.ggv.instau.ui.theme.MediumSpacing
import ayush.ggv.instau.ui.theme.SocialAppTheme
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import instaU.ayush.com.model.LikeParams
import org.koin.androidx.compose.koinViewModel
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun PostListItem(
    modifier: Modifier = Modifier,
    post: Post,
    onPostClick: (Post) -> Unit,
    onProfileClick: (Long) -> Unit,
    onLikeClick: () -> Unit,
    onCommentClick: (Long) -> Unit,
    isDetailScreen: Boolean = false
) {
    val viewModel: PostListItemViewModel = koinViewModel()
    val homeScreenViewModel: HomeScreenViewModel = koinViewModel()
    val profileScreenViewModel: ProfileScreenViewModel = koinViewModel()
    val navHostController = rememberNavController()
    val currentDestination = navHostController.currentDestinationAsState().value

    val isPostLiked by viewModel.isPostLiked.observeAsState(initial = post.isLiked)
    val likesCount by viewModel.likesCount.observeAsState(initial = post.likesCount)

    fun formatTimeAgo(time: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        val dateTime = LocalDateTime.parse(time, formatter)
        val zonedDateTime = dateTime.atZone(ZoneId.systemDefault())
        val now = ZonedDateTime.now()

        val diff = ChronoUnit.SECONDS.between(zonedDateTime, now)

        return when {
            diff < 60 -> "$diff seconds ago"
            diff < 3600 -> "${diff / 60} minutes ago"
            diff < 86400 -> "${diff / 3600} hours ago"
            diff < 604800 -> "${diff / 86400} days ago"
            else -> "${diff / 604800} weeks ago"
        }
    }


    // Format the LocalDateTime object into a "time ago" string
    val timeAgo = formatTimeAgo(post.createdAt)

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
            .clip(shape = RoundedCornerShape(16.dp))
    ) {
        PostItemHeader(
            name = post.userName,
            profileUrl = post.userImageUrl ?: "",
            date = timeAgo,
            onProfileClick = {
                onProfileClick(post.userId)
            },
            isOwnPost = post.isOwnPost,
            postImage = post.imageUrl,
            onDelete = {
                viewModel.deletePost(post.postId, post.imageUrl)
                if (currentDestination?.route == HomeDestination.route) {
                    homeScreenViewModel.fetchData()
                } else if (currentDestination?.route == PostDetailDestination.route) {
                    navHostController.navigateUp()
                } else {
                    profileScreenViewModel.fetchProfile(post.userId,)
                }
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
            onLikeClick = {
                viewModel.likePost(
                likeParams = LikeParams(
                    postId = post.postId
                )
            )},
            onCommentClick = { onCommentClick(post.postId) },
            likesCount = likesCount,
            commentsCount = post.commentsCount,
            isPostLiked = isPostLiked,
        )
        Text(
            text = post.caption,
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
    postImage: String,
    date: String,
    onProfileClick: () -> Unit,
    isOwnPost: Boolean? = false,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val (showMenu, setShowMenu) = remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
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
            modifier = modifier.size(30.dp)
        )
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

        IconButton(onClick = { setShowMenu(true) }) {
            Icon(
                painter = painterResource(id = R.drawable.round_more_horiz_24),
                contentDescription = null,
                tint = if (MaterialTheme.colors.isLight) {
                    LightGray
                } else {
                    DarkGray
                },
            )
        }
        // Add this DropdownMenu
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { setShowMenu(false) }
        ) {
            DropdownMenuItem(onClick = {
                setShowMenu(false)
            }) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            onDelete()
                            Toast.makeText(context, "post Deleted ", Toast.LENGTH_SHORT).show()
                        }) {
                        if (isOwnPost == true) {
                            Text(
                                text = "Delete a post",
                                style = MaterialTheme.typography.body2,
                                color = if (MaterialTheme.colors.isLight) {
                                    Color.Red
                                } else {
                                    Color.Red
                                },
                                modifier = Modifier.weight(1f) // Add this line
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier
                            .padding(start = 6.dp, end = 6.dp)

                    )
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            // Copy the image URL to the clipboard
                            clipboardManager.setText(AnnotatedString(profileUrl))
                            Toast.makeText(context, "image url copied ", Toast.LENGTH_SHORT).show()


                        }) {
                        Text(
                            text = "Copy Image URL",
                            style = MaterialTheme.typography.body2,

                            modifier = Modifier.weight(1f) // Add this line
                        )
                        Spacer(modifier = Modifier.width(30.dp)) // Add some spacing between the icon and the text

                        Icon(
                            painter = painterResource(id = R.drawable.paper_clip_svgrepo_com), // Replace with your actual clip icon resource
                            contentDescription = "Copy Icon",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier
                            .padding(start = 6.dp, end = 6.dp)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            // Download the image to the device
                            val request = DownloadManager.Request(Uri.parse(postImage))
                                .setTitle("Download")
                                .setDescription("Downloading")
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                .setDestinationInExternalPublicDir(
                                    Environment.DIRECTORY_DOWNLOADS,
                                    "${System.currentTimeMillis()}"
                                )
                                .setAllowedOverMetered(true)
                                .setAllowedOverRoaming(true)

                            val downloadManager =
                                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                            downloadManager.enqueue(request)
                            Toast.makeText(context, "Post Downloaded", Toast.LENGTH_SHORT).show()
                        }) {
                        Text(
                            text = "Download Image",
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(30.dp)) // Add some spacing between the icon and the text
                        Icon(
                            painter = painterResource(id = R.drawable.download_svgrepo_com), // Replace with your actual clip icon resource
                            contentDescription = "Copy Icon",
                            modifier = Modifier.size(18.dp)
                        )
                    }

                }
            }
            // Add more items here if needed
        }
    }
}


@Composable
fun PostLikeRow(
    modifier: Modifier = Modifier,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    likesCount: Int,
    commentsCount: Int,
    isPostLiked: Boolean // Add this line
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
                painter = painterResource(id = if (isPostLiked) R.drawable.liked_heart_drawable else R.drawable.like_icon_outlined),
                contentDescription = null,
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