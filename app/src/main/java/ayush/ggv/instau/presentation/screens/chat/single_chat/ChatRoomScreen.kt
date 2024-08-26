package ayush.ggv.instau.presentation.screens.chat.single_chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatRoomScreen(
    friendName: String,
    friendAvatar: String,
    userAvatar: String,
    userId: Long,
    chatState: ChatRoomHistoryState,
    messageText : String,
    onMessageChange : (String) -> Unit,
    onSendClick : () -> Unit,
    onBackClick :() -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {

        EnhancedAppBar(friendName, friendAvatar, onBackClick)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
                .fillMaxSize(),
            reverseLayout = true
        ) {
            val groupByTimestampHistoryList = chatState.data.groupBy { it.formattedDate }

            groupByTimestampHistoryList.forEach { (date, messages) ->
                items(messages) { message ->
                    val isSender = message.sender == userId
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = if (isSender)
                            Alignment.CenterEnd
                        else
                            Alignment.CenterStart
                    ) {
                        MessageBubble(
                            message = message,
                            isSender = isSender,
                            senderAvatar = userAvatar,
                            receiverAvatar = friendAvatar
                        )
                    }
                }
                stickyHeader {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date.orEmpty(),
                            style = MaterialTheme.typography.body2
                                .copy(color = MaterialTheme.colors.onBackground)
                        )
                    }
                }
            }
        }
        MessageInput(
            messageText = messageText,
            onMessageChange = onMessageChange,
            onSendClick = onSendClick
        )



    }
}

@Composable
fun MessageInput(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp)),
        border = BorderStroke(
            width = 0.5.dp,
            color = MaterialTheme.colors.onBackground.copy(alpha = 0.2f)
        ),
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(8.dp) // Rounded corners for a modern look
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                value = messageText,
                onValueChange = onMessageChange,
                placeholder = {
                    Text(
                        text = "Type message...",
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.3f)
                    )
                },
                singleLine = false,
                maxLines = 4,
                shape = RoundedCornerShape(8.dp), // Rounded corners for TextField

            )

            IconButton(
                onClick = onSendClick,
                modifier = Modifier
                    .size(48.dp) // Increase the size of the button for better touch area
                    .clip(CircleShape) // Circular shape for the button
                    .background(MaterialTheme.colors.primary) // Background color for the button
                    .padding(8.dp) // Padding inside the button
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send message",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}

@Composable
fun EnhancedAppBar(friendName: String, friendAvatar: String, onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberAsyncImagePainter(model = friendAvatar),
                    contentDescription = "Friend avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = friendName)
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = { /* Implement more options */ }) {
                Icon(Icons.Default.MoreVert, contentDescription = "More options")
            }
        }
    )
}
@Composable
fun OutlinedCard(
    modifier: Modifier = Modifier,
    border: BorderStroke? = null,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = 1.dp,
    shape: Shape = MaterialTheme.shapes.medium,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        border = border,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation = elevation,
        shape = shape,
        content = content
    )
}
