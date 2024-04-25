package ayush.ggv.instau.presentation.home.onboarding

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ayush.ggv.instau.common.components.CircleImage
import ayush.ggv.instau.common.components.FollowsButton
import ayush.ggv.instau.common.fakedata.FollowsUser
import ayush.ggv.instau.ui.theme.MediumSpacing
import ayush.ggv.instau.R
import ayush.ggv.instau.common.fakedata.sampleUsers
import ayush.ggv.instau.ui.theme.SmallSpacing
import ayush.ggv.instau.ui.theme.SocialAppTheme

@Composable
fun OnBoardingUserItem(
    modifier: Modifier = Modifier,
    followsUser: FollowsUser,
    onUserClick: (FollowsUser) -> Unit,
    isFollowing: Boolean = false,
    onFollowButtonClick: (Boolean, FollowsUser) -> Unit
) {
    Card(
        modifier = modifier
            .size(height = 140.dp, width = 130.dp)
            .clickable {
                onUserClick(followsUser)
            },
        elevation = 0.dp
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(MediumSpacing),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircleImage(
                modifier = modifier.size(50.dp),
                imageUrl = followsUser.profileUrl,
                onClick = {}
            )

            Spacer(modifier = modifier.height(SmallSpacing))

            Text(
                text = followsUser.name,
                style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Medium),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = modifier.height(MediumSpacing))

            FollowsButton(
                text = R.string.follow_button_label,
                onFollowButtonClick = { onFollowButtonClick(!isFollowing, followsUser) },
                modifier = modifier
                    .heightIn(30.dp)
                    .widthIn(100.dp),
                isOutline = isFollowing
            )
        }
    }
}
@Preview( uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewOnBoardingUserItem() {
    SocialAppTheme {
        OnBoardingUserItem(
            followsUser = sampleUsers.first(),
            onUserClick = {},
            onFollowButtonClick = { _, _ -> })
    }
}