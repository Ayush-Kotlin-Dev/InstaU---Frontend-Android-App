package ayush.ggv.instau.presentation.account.follows

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter

@Composable
fun FollowsScreen(
    modifier : Modifier = Modifier,
    uiState: FollowsUiState,
    fetchFollows  : () -> Unit,
    onItemClick : (Int) -> Unit

) {
    //Open Image in dialog
    var showDialog by remember { mutableStateOf(false) }
    var imageUrl by remember { mutableStateOf("") }
    //

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center

    ){
        LazyColumn (
            modifier = modifier
                .fillMaxSize()
        ){
            items(
                items = uiState.followUsers,
                key = { user -> user.id }
            ){ user ->
                FollowsListItem(
                    name = user.name,
                    bio = user.bio,
                    imageUrl = user.profileUrl,
                    onItemClick = { onItemClick(user.id) },
                    onImageClick = {
                        imageUrl = user.profileUrl
                        showDialog = true
                    }
                )
            }

        }
        if(uiState.isLoading && uiState.followUsers.isEmpty()){
            CircularProgressIndicator()
        }
    }
    LaunchedEffect(
        key1 = Unit,
        block = {
            fetchFollows()
        }
    )
    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(dismissOnClickOutside = true)
        ) {
            Box(
                modifier = Modifier
                    .size(350.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(10.dp)) // Change the shape of the dialog box


            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}