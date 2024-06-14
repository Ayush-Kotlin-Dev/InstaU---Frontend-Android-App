package ayush.ggv.instau.presentation.screens.chat.single_chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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

//@Preview
//@Composable
//fun PreviewMessageBubble() {
//    MessageBubble(
//        message = RoomHistoryList.Message(
//            sessionId = "1",
//            receiver = "1",
//            sender = "2",
//            textMessage = "Hello",
//            timestamp = 1633660800000,
//            formattedTime = "12:00",
//            formattedDate = "2021-10-08"
//        ),
//        isSender = false,
//        senderAvatar = "1",
//        receiverAvatar = "2"
//    )
//}
//
//@Preview
//@Composable
//fun PreviewAvatarHead() {
//    AvatarHead(
//        message = RoomHistoryList.Message(
//            sessionId = "1",
//            receiver = "1",
//            sender = "2",
//            textMessage = "Hello",
//            timestamp = 1633660800000,
//            formattedTime = "12:00",
//            formattedDate = "2021-10-08"
//        ),
//        senderAvatar = "data:image/jpeg;base64,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",
//        receiverAvatar = "data:image/jpeg;base64,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",
//        isSender = false
//    )
//}