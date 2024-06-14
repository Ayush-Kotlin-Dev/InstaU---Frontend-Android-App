package ayush.ggv.instau.presentation.screens.chat.single_chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
    onMessageChange : (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Box(Modifier.fillMaxWidth()) {

            OutlinedButton(
                modifier = Modifier.align(Alignment.CenterStart),
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(8.dp),
            ) {
                Icon(
                    modifier = Modifier.padding(start = 8.dp),
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back button"
                )
            }

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = friendName,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
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

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            border = BorderStroke(
                width = 0.5.dp,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.2f)
            ),
            backgroundColor = MaterialTheme.colors.background
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
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
                colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = MaterialTheme.colors.background,
                    focusedIndicatorColor = MaterialTheme.colors.background,
                    unfocusedIndicatorColor = MaterialTheme.colors.background,
                    disabledIndicatorColor = MaterialTheme.colors.background,
                    backgroundColor = MaterialTheme.colors.background
                ),
                trailingIcon = {
                    Icon(
                        modifier = Modifier
//                            .clickable { viewModel.sendMessage() }
                            .rotate(-45f),
                        imageVector = Icons.Filled.Send,
                        contentDescription = "Send message",
                        tint = MaterialTheme.colors.onBackground.copy(alpha = 0.3f)
                    )
                }
            )
        }
    }
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
