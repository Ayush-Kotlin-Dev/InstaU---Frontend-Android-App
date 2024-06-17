package ayush.ggv.instau.presentation.screens.chat.friends_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import ayush.ggv.instau.R

@Composable
fun FriendListScreen(
    friendListState: FriendListState,
    onNavigateToChatScreen: (Long,String,String) -> Unit,
    onGroupChatClick : () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(8.dp),
                    onClick = {
                        onGroupChatClick()
                    }) {
                    Text(text = "Group Chat")
                }

                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = "Chats",
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
                )
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search",
                    )
                },
                placeholder = { Text(text = "Search User") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                value = "",
                onValueChange = {}
            )

            if (friendListState.data.isNotEmpty())
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp)
                ) {
                    val friendList = friendListState.data

                    friendList.forEach {
                        item {
                            FriendListItemRow(friendData = it , onNavigateToChatScreen = onNavigateToChatScreen)
                        }
                    }
                }
        }

        if (friendListState.data.isEmpty())
            Image(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(42.dp),
                painter = painterResource(id = R.drawable.bg_friend_list_empty),
                contentDescription = "No friends!"
            )

        if (friendListState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
