package ayush.ggv.instau.presentation.screens.account.profile

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    isFollowing: Boolean
) {

    val pullRefreshState = rememberPullRefreshState(
        refreshing = userInfoUiState.isLoading && profilePostsUiState.isLoading,
        onRefresh = { fetchData() })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(state = pullRefreshState)
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            item {
                if (userInfoUiState.isLoading) {
                    ShimmerProfileScreenPlaceholder()
                } else {
                    ProfileHeaderSection(
                        imageUrl = userInfoUiState.profile?.imageUrl ?: "",
                        name = userInfoUiState.profile?.name ?: "",
                        bio = userInfoUiState.profile?.bio ?: "Stay Tuned... ",
                        followersCount = userInfoUiState.profile?.followersCount ?: 0,
                        followingCount = userInfoUiState.profile?.followingCount ?: 0,
                        postsCount = profilePostsUiState.posts.size,
                        onButtonClick = {
                            if (userInfoUiState.profile?.isOwnProfile == true) {
                                navigator.navigate(
                                    EditProfileDestination(
                                        userInfoUiState.profile.id,
                                    )
                                ) // Navigate to EditProfileDestination if the profile belongs to the current user
                            } else {
                                onButtonClick()
//                            isFollowing = !isFollowing
                            }
                        },
                        onFollowersClick = onFollowersClick,
                        onFollowingClick = onFollowingClick,
                        isCurrentUser = userInfoUiState.profile?.isOwnProfile ?: false,
                        isFollowing = isFollowing
                    )
                }
            }

            if (profilePostsUiState.isLoading && userInfoUiState.profile != null) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                items(
                    items = profilePostsUiState.posts,
                    key = { post -> post.postId }
                ) { post ->
                    PostListItem(
                        post = post,
                        onPostClick = onPostClick,
                        onProfileClick = { navigator.navigate(ProfileDestination(post.userId)) },
                        onLikeClick = onLikeClick,
                        onCommentClick = onCommentClick
                    )
                }
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
fun ProfileHeaderSection(
    modifier: Modifier = Modifier,
    imageUrl: String,
    name: String,
    bio: String,
    followersCount: Int,
    followingCount: Int,
    postsCount: Int,
    isCurrentUser: Boolean = false,
    isFollowing: Boolean = false,
    onButtonClick: () -> Unit,  //Edit Button for Admin , and Follow Button for other users
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit

) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .background(color = MaterialTheme.colors.surface)
            .padding(all = LargeSpacing)
    ) {
        CircleImage(
            modifier = Modifier.size(90.dp),
            imageUrl = imageUrl,
            onClick = { showDialog = true }
        )
        Spacer(modifier = modifier.height(SmallSpacing))
        Text(
            text = name,
            style = MaterialTheme.typography.subtitle1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = bio,
            style = MaterialTheme.typography.body2,
            maxLines = 2, overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = modifier.height(SmallSpacing))

        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FollowText(
                    count = followersCount,
                    text = R.string.followers_text,
                    onClick = onFollowersClick
                )
                Spacer(modifier = modifier.width(MediumSpacing))
                FollowText(
                    count = followingCount,
                    text = R.string.following_text,
                    onClick = onFollowingClick
                )
            }
            if (isCurrentUser) {
                FollowsButton(
                    text = R.string.editProfileLabel,
                    onFollowButtonClick = onButtonClick,
                    modifier = modifier
                        .height(30.dp)
                        .widthIn(min = 100.dp),
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
                    modifier = modifier
                        .height(30.dp)
                        .widthIn(min = 100.dp),
                    isOutline = isFollowing
                )
            }
        }
        Spacer(modifier = modifier.height(SmallSpacing))
        FollowText(
            count = postsCount,
            text = R.string.posts_count_text,
            onClick = {}
        )
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
                    .clip(RoundedCornerShape(16.dp)) // Change the shape of the dialog box
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
fun FollowText(
    modifier: Modifier = Modifier,
    count: Int,
    @StringRes text: Int,
    onClick: () -> Unit
) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            ) {
                append(text = "$count ")
            }
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            ) {
                append(text = stringResource(id = text))
            }
        },
        modifier = modifier.clickable(onClick = onClick)
    )
}