package ayush.ggv.instau.presentation.screens.home.onboarding

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ayush.ggv.instau.R
import ayush.ggv.instau.presentation.components.CircleImage
import ayush.ggv.instau.presentation.components.FollowsButton
import ayush.ggv.instau.ui.theme.MediumSpacing
import ayush.ggv.instau.ui.theme.SmallSpacing
import instaU.ayush.com.model.FollowUserData

@Composable
fun OnBoardingUserItem(
    modifier: Modifier = Modifier,
    followsUser: FollowUserData,
    onUserClick: (Long) -> Unit,
    onFollowButtonClick: () -> Unit
) {
    // Create a mutable state for the following status
    val isFollowing = remember { mutableStateOf(followsUser.isFollowing) }
    val context = LocalContext.current
    Card(
        modifier = modifier
            .size(height = 140.dp, width = 130.dp)
            .clickable {
                onUserClick(followsUser.id)
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
                imageUrl = followsUser.imageUrl ?: "",
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
                text = if (isFollowing.value) R.string.unfollow_button_label else R.string.follow_button_label,
                onFollowButtonClick = {
                    isFollowing.value = !isFollowing.value
                    onFollowButtonClick()
                    Toast.makeText(
                        context,
                        if (isFollowing.value) "Unfollowed" else "Followed",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = modifier
                    .height(30.dp)
                    .widthIn(min = 100.dp),
                isOutline = isFollowing.value
            )
        }
    }
}
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun PreviewOnBoardingUserItem() {
//    SocialAppTheme {
//        OnBoardingUserItem(
//            followsUser = sampleUsers.first(),
//            onUserClick = {},
//            onFollowButtonClick = { _, _ -> },
//        )
//    }
//}