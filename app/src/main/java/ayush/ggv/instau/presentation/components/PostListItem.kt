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
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
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
import ayush.ggv.instau.util.formatTimeAgo
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

    val timeAgo = formatTimeAgo(post.createdAt)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { onPostClick(post) },
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            UpgradedPostItemHeader(
                name = post.userName,
                profileUrl = post.userImageUrl ?: "",
                date = timeAgo,
                onProfileClick = { onProfileClick(post.userId) },
                isOwnPost = post.isOwnPost,
                postImage = post.imageUrl,
                onDelete = {
                    viewModel.deletePost(post.postId, post.imageUrl)
                    when (currentDestination?.route) {
                        HomeDestination.route -> homeScreenViewModel.fetchData()
                        PostDetailDestination.route -> navHostController.navigateUp()
                        else -> profileScreenViewModel.fetchProfile(post.userId)
                    }
                }
            )

            AsyncImage(
                model = post.imageUrl,
                contentDescription = "Post image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )

            UpgradedPostLikeRow(
                onLikeClick = {
                    viewModel.likePost(LikeParams(postId = post.postId))
                },
                onCommentClick = { onCommentClick(post.postId) },
                likesCount = likesCount,
                commentsCount = post.commentsCount,
                isPostLiked = isPostLiked,
            )

            Text(
                text = post.caption,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                maxLines = if (isDetailScreen) 20 else 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun UpgradedPostItemHeader(
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
    var showMenu by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleImage(
            imageUrl = profileUrl,
            modifier = Modifier
                .size(40.dp)
        ){
            onProfileClick()
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = date,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }

        IconButton(onClick = { showMenu = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More options"
            )
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            if (isOwnPost == true) {
                DropdownMenuItem(onClick = {
                    onDelete()
                    showMenu = false
                    Toast.makeText(context, "Post deleted", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Delete post", color = MaterialTheme.colors.error)
                }
                Divider()
            }

            DropdownMenuItem(onClick = {
                clipboardManager.setText(AnnotatedString(postImage))
                showMenu = false
                Toast.makeText(context, "Image URL copied", Toast.LENGTH_SHORT).show()
            }) {
                Text("Copy Image URL")
            }

            DropdownMenuItem(onClick = {
                val request = DownloadManager.Request(Uri.parse(postImage))
                    .setTitle("Download")
                    .setDescription("Downloading image")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${System.currentTimeMillis()}.jpg")

                val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                downloadManager.enqueue(request)
                showMenu = false
                Toast.makeText(context, "Image download started", Toast.LENGTH_SHORT).show()
            }) {
                Text("Download Image")
            }
        }
    }
}

@Composable
fun UpgradedPostLikeRow(
    modifier: Modifier = Modifier,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    likesCount: Int,
    commentsCount: Int,
    isPostLiked: Boolean
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconToggleButton(
            checked = isPostLiked,
            onCheckedChange = { onLikeClick() }
        ) {
            Icon(
                imageVector = if (isPostLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = if (isPostLiked) "Unlike" else "Like",
                tint = if (isPostLiked) Color.Red else MaterialTheme.colors.onSurface
            )
        }
        Text(
            text = likesCount.toString(),
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.padding(start = 4.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        IconButton(onClick = onCommentClick) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_chat_bubble_24),
                contentDescription = "Comment"
            )
        }
        Text(
            text = commentsCount.toString(),
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}