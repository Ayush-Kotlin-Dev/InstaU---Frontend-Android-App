package ayush.ggv.instau.presentation.screens.chat.single_chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ayush.ggv.instau.R
import ayush.ggv.instau.model.friendList.RoomHistoryList
import coil.compose.rememberAsyncImagePainter

@Composable
fun MessageBubble(
    message: RoomHistoryList.Message,
    isSender: Boolean,
    senderAvatar: String,
    receiverAvatar: String
) {
    val radius = if (isSender) {
        RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp, topEnd = 4.dp, bottomEnd = 16.dp)
    } else {
        RoundedCornerShape(topStart = 4.dp, bottomStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp)
    }

    val bubbleColor = if (isSender) {
        MaterialTheme.colors.secondary.copy(alpha = 0.9f)
    } else {
        MaterialTheme.colors.primary.copy(alpha = 0.9f)
    }

    val textColor = if (isSender) {
        MaterialTheme.colors.onSecondary
    } else {
        MaterialTheme.colors.onPrimary
    }

    Row(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = if (isSender) Arrangement.End else Arrangement.Start
    ) {
        if (!isSender) {
            AvatarHead(receiverAvatar)
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (isSender) Alignment.End else Alignment.Start
        ) {
            Surface(
                color = bubbleColor,
                shape = radius,
                elevation = 1.dp
            ) {
                Text(
                    modifier = Modifier.padding(12.dp),
                    text = message.textMessage.orEmpty(),
                    color = textColor,
                    style = MaterialTheme.typography.body1
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = message.formattedTime.orEmpty(),
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f)
            )
        }

        if (isSender) {
            Spacer(modifier = Modifier.width(8.dp))
            AvatarHead(senderAvatar)
        }
    }
}

@Composable
fun AvatarHead(avatar: String) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.surface)
            .border(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f), CircleShape)
    ) {
        Image(
            painter = if (avatar.isNotEmpty()) rememberAsyncImagePainter(avatar) else painterResource(id = R.drawable.person_circle_icon),
            contentDescription = "Avatar",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
