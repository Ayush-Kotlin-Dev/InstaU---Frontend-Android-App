package ayush.ggv.instau.presentation.account.profile

import androidx.annotation.StringRes
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ayush.ggv.instau.R
import ayush.ggv.instau.common.components.CircleImage
import ayush.ggv.instau.common.components.FollowsButton
import ayush.ggv.instau.common.components.PostListItem
import ayush.ggv.instau.common.fakedata.Post
import ayush.ggv.instau.ui.theme.LargeSpacing
import ayush.ggv.instau.ui.theme.MediumSpacing
import ayush.ggv.instau.ui.theme.SmallSpacing
import ayush.ggv.instau.ui.theme.SocialAppTheme


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userInfoUiState: UserInfoUiState,
    profilePostsUiState: ProfilePostUiState,
    onButtonClick: () -> Unit,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit,
    onPostClick: (Post) -> Unit,
    onLikeClick: (String) -> Unit,
    onCommentClick: (String) -> Unit,
    fetchData: () -> Unit
) {
    if(userInfoUiState.isLoading && profilePostsUiState.isLoading){
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(all = MediumSpacing)
        ) {
            item(key = "header_section"){
                ProfileHeaderSection(
                    imageUrl = userInfoUiState.profile?.profileUrl?: "",
                    name = userInfoUiState.profile?.name ?: "",
                    bio = userInfoUiState.profile?.bio ?: "ayush",
                    followersCount = userInfoUiState.profile?.followersCount ?: 0,
                    followingCount = userInfoUiState.profile?.followingCount ?: 0,
                    onButtonClick = onButtonClick,
                    onFollowersClick = onFollowersClick,
                    onFollowingClick = onFollowingClick

                )
            }

            items(
                items = profilePostsUiState.posts,
                key = { post -> post.id }
            ){ post ->
                PostListItem(
                    post = post,
                    onPostClick = onPostClick,
                    onProfileClick = {},
                    onLikeClick = onLikeClick,
                    onCommentClick = onCommentClick
                )
            }
        }
    }
    LaunchedEffect (key1 = Unit){
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
    isCurrentUser: Boolean = false,
    isFollowing: Boolean = false,
    onButtonClick: () -> Unit,  //Edit Button for Admin , and Follow Button for other users
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit

) {
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
            onClick = {}
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
                    text = R.string.followers_text,
                    onClick = onFollowingClick
                )
            }
            FollowsButton(
                text = R.string.follow_button_label,
                onFollowButtonClick = onButtonClick,
                modifier = modifier
                    .height(30.dp)
                    .widthIn(min = 100.dp),
                isOutline = isCurrentUser || isFollowing
            )
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

@Composable
@Preview
fun ProfileHeaderSectionPreview() {
    SocialAppTheme {
        Surface(color = MaterialTheme.colors.surface) {
            ProfileHeaderSection(
                imageUrl = "",
                name = "Ayush",
                bio = "Android Developer at GGV , Studying BCA at GGV Bilaspur ",
                followersCount = 100,
                followingCount = 200,
                isCurrentUser = false,
                isFollowing = false,
                onButtonClick = {},
                onFollowersClick = {},
                onFollowingClick = {}
            )
        }
    }
}
