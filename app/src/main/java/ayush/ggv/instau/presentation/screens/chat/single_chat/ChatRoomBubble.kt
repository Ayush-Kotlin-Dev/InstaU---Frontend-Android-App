package ayush.ggv.instau.presentation.screens.chat.single_chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ayush.ggv.instau.model.friendList.RoomHistoryList
import coil.compose.rememberAsyncImagePainter

@Composable
fun MessageBubble(
    message: RoomHistoryList.Message,
    isSender: Boolean,
    senderAvatar: String,
    receiverAvatar: String
) {

    val radius =
        if (isSender) RoundedCornerShape(
            topStart = 16.dp,
            bottomStart = 16.dp,
            topEnd = 0.dp,
            bottomEnd = 16.dp
        ) else RoundedCornerShape(
            topStart = 0.dp,
            bottomStart = 16.dp,
            topEnd = 16.dp,
            bottomEnd = 16.dp
        )

    Row(
        modifier = Modifier
            .padding(bottom = 24.dp)
    ) {

        if (isSender.not()) {
            AvatarHead(message, senderAvatar, receiverAvatar, isSender)
        }

        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .weight(0.8f)
                .wrapContentSize(align = if (isSender) Alignment.CenterEnd else Alignment.CenterStart)
                .background(
                    color = if (isSender)
                        MaterialTheme.colors.secondary
                    else
                        MaterialTheme.colors.primary,
                    shape = radius
                )
                .padding(12.dp),
            text = message.textMessage.orEmpty(),
            color = if (isSender)
                MaterialTheme.colors.onSecondary
            else
                MaterialTheme.colors.onPrimary,
            fontWeight = FontWeight.Normal

        )

        if (isSender) {
            AvatarHead(message, senderAvatar, receiverAvatar, true)
        }

    }
}

@Composable
fun AvatarHead(
    message: RoomHistoryList.Message,
    senderAvatar: String,
    receiverAvatar: String,
    isSender: Boolean
) {
    val avatar =
        if (isSender) senderAvatar else receiverAvatar
    Column(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(35.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(color = MaterialTheme.colors.onBackground),
            painter = rememberAsyncImagePainter(model = avatar),
            contentDescription = "Friend avatar"
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = message.formattedTime.orEmpty(),
            color = Color.White,
            style = MaterialTheme.typography.body2
                .copy(
                    fontSize = 10.sp,
                    color = MaterialTheme.colors.onBackground
                )
        )
    }
}
