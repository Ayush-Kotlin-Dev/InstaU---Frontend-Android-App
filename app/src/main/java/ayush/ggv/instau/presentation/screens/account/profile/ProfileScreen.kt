package ayush.ggv.instau.presentation.screens.account.profile

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ayush.ggv.instau.R
import ayush.ggv.instau.model.Post
import ayush.ggv.instau.presentation.components.CircleImage
import ayush.ggv.instau.presentation.components.FollowsButton
import ayush.ggv.instau.presentation.components.PostListItem
import ayush.ggv.instau.presentation.components.ShimmerProfileScreenPlaceholder
import ayush.ggv.instau.presentation.screens.destinations.EditProfileDestination
import ayush.ggv.instau.presentation.screens.destinations.ProfileDestination
import ayush.ggv.instau.ui.theme.LargeSpacing
import ayush.ggv.instau.ui.theme.MediumSpacing
import ayush.ggv.instau.ui.theme.SmallSpacing
import ayush.ggv.instau.ui.theme.SocialAppTheme
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userInfoUiState: UserInfoUiState,
    profilePostsUiState: ProfilePostUiState,
    onButtonClick: () -> Unit,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit,
    onPostClick: (Post) -> Unit,
    onLikeClick: () -> Unit,
    onCommentClick: (Long) -> Unit,
    fetchData: () -> Unit,
    navigator: DestinationsNavigator,
    isFollowing: Boolean,
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = userInfoUiState.isLoading && profilePostsUiState.isLoading,
        onRefresh = { fetchData() }
    )
    val scrollState = rememberLazyListState()
    val headerHeight by animateDpAsState(
        targetValue = max(
            100.dp, // minHeaderHeight
            300.dp - remember { derivedStateOf { scrollState.firstVisibleItemScrollOffset.dp } }.value
        ), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(state = pullRefreshState)
    ) {
        LazyColumn(
            state = scrollState,
            modifier = modifier.fillMaxSize()
        ) {
            item {
                if (userInfoUiState.isLoading) {
                    ShimmerProfileScreenPlaceholder()
                } else {
                    ProfileHeader(
                        headerHeight = headerHeight,
                        userInfoUiState = userInfoUiState,
                        onButtonClick = onButtonClick,
                        isFollowing = isFollowing,
                        navigator = navigator
                    )
                }
            }

            item {
                UserStatistics(
                    followersCount = userInfoUiState.profile?.followersCount ?: 0,
                    followingCount = userInfoUiState.profile?.followingCount ?: 0,
                    postsCount = profilePostsUiState.posts.size,
                    onFollowersClick = onFollowersClick,
                    onFollowingClick = onFollowingClick
                )
            }

            item {
                ProfileContent(
                    posts = profilePostsUiState.posts,
                    mentions = emptyList(), // You'll need to provide this data
                    likes = emptyList(), // You'll need to provide this data
                    onPostClick = onPostClick
                )
            }
        }
        PullRefreshIndicator(
            refreshing = userInfoUiState.isLoading && profilePostsUiState.isLoading,
            state = pullRefreshState,
            modifier = modifier.align(Alignment.TopCenter)
        )
    }
    LaunchedEffect(Unit) {
        fetchData()
    }
}

@Composable
fun ProfileHeader(
    headerHeight: Dp,
    userInfoUiState: UserInfoUiState,
    onButtonClick: () -> Unit,
    isFollowing: Boolean,
    navigator: DestinationsNavigator
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(headerHeight)
    ) {
        // Add a background image or gradient
        Image(
            painter = painterResource(id = R.drawable.ad_ex_3),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Overlay a semi-transparent scrim for better text visibility
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )
        ProfileHeaderSection(
            imageUrl = userInfoUiState.profile?.imageUrl ?: "",
            name = userInfoUiState.profile?.name ?: "",
            bio = userInfoUiState.profile?.bio ?: "Stay Tuned... ",
            onButtonClick = {
                if (userInfoUiState.profile?.isOwnProfile == true) {
                    navigator.navigate(
                        EditProfileDestination(
                            userInfoUiState.profile.id,
                        )
                    )
                } else {
                    onButtonClick()
                }
            },
            isCurrentUser = userInfoUiState.profile?.isOwnProfile ?: false,
            isFollowing = isFollowing
        )
    }
}

@Composable
fun ProfileHeaderSection(
    modifier: Modifier = Modifier,
    imageUrl: String,
    name: String,
    bio: String,
    isCurrentUser: Boolean = false,
    isFollowing: Boolean = false,
    onButtonClick: () -> Unit
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .background(color = MaterialTheme.colors.surface.copy(alpha = 0.7f))
            .padding(all = LargeSpacing)
    ) {
        CircleImage(
            modifier = Modifier
                .size(120.dp)
                .border(
                    width = 4.dp,
                    color = MaterialTheme.colors.surface,
                    shape = CircleShape
                ),
            imageUrl = imageUrl,
            onClick = { showDialog = true }
        )
        Spacer(modifier = Modifier.height(SmallSpacing))
        Text(
            text = name,
            style = MaterialTheme.typography.h5,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = bio,
            style = MaterialTheme.typography.body1,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(SmallSpacing))

        if (isCurrentUser) {
            FollowsButton(
                text = R.string.editProfileLabel,
                onFollowButtonClick = onButtonClick,
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                isOutline = true
            )
        } else {
            FollowsButton(
                text = if (isFollowing) R.string.unfollow_button_label else R.string.follow_button_label,
                onFollowButtonClick = {
                    onButtonClick()
                    Toast.makeText(
                        context,
                        if (isFollowing) "Unfollowed" else "Followed",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                isOutline = isFollowing
            )
        }
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(dismissOnClickOutside = true)
        ) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}


@Composable
fun UserStatistics(
    followersCount: Int,
    followingCount: Int,
    postsCount: Int,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(
            icon = Icons.Default.Person,
            count = followersCount,
            label = stringResource(R.string.followers_text),
            onClick = onFollowersClick
        )
        StatItem(
            icon = Icons.Default.Add,
            count = followingCount,
            label = stringResource(R.string.following_text),
            onClick = onFollowingClick
        )
        StatItem(
            icon = Icons.Default.AccountBox,
            count = postsCount,
            label = stringResource(R.string.posts_count_text),
            onClick = {}
        )
    }
}

@Composable
fun StatItem(
    icon: ImageVector,
    count: Int,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )
        Text(text = label, style = MaterialTheme.typography.caption)
    }
}
@Composable
fun ProfileContent(
    posts: List<Post>,
    mentions: List<Post>,
    likes: List<Post>,
    onPostClick: (Post) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Posts", "Mentions", "Likes")

    Column {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                )
            }
        }

        when (selectedTabIndex) {
            0 -> PostGrid(posts = posts, onPostClick = onPostClick)
            1 -> PostGrid(posts = mentions, onPostClick = onPostClick)
            2 -> PostGrid(posts = likes, onPostClick = onPostClick)
        }
    }
}

@Composable
fun PostGrid(posts: List<Post>, onPostClick: (Post) -> Unit) {
    Column {
        posts.chunked(3).forEach { rowPosts ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowPosts.forEach { post ->
                    PostThumbnail(
                        post = post,
                        onClick = { onPostClick(post) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill empty spaces if the row is not complete
                repeat(3 - rowPosts.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun PostThumbnail(post: Post, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val imageModifier = modifier
        .aspectRatio(1f) // This ensures the image is a square
        .padding(2.dp)
        .clickable(onClick = onClick)

    AsyncImage(
        model = post.imageUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = imageModifier
    )
}
