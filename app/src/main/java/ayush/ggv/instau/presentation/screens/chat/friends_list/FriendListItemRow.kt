package ayush.ggv.instau.presentation.screens.chat.friends_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ayush.ggv.instau.model.FriendList
import ayush.ggv.instau.util.getTimeAgo
import coil.compose.rememberAsyncImagePainter


@Composable
fun FriendListItemRow(friendData: FriendList.FriendInfo) {

    Row(
        Modifier
            .fillMaxWidth()
            .clickable {
//                navigateTo(
//                    navController = navController,
//                    "${Routes.ChatRoom.route}/$args"
//                )
            }
            .height(60.dp)
    ) {
        Image(
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(color = MaterialTheme.colors.onBackground),
            painter = rememberAsyncImagePainter(friendData.avatar),
            contentDescription = "Friend avatar"
        )

        Column(
            modifier = Modifier
                .weight(0.7f)
                .padding(start = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = friendData.username,
                    style = MaterialTheme.typography.h2.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = if (friendData.lastMessage?.timestamp != null) ""  else "",
                    style = MaterialTheme.typography.caption
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 40.dp),
                text = friendData.lastMessage?.textMessage ?: "...",
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body1.copy(
                    fontSize = 11.sp
                ),
                maxLines = 1
            )
        }
    }
}