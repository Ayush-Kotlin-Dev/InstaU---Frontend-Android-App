package ayush.ggv.instau.presentation.screens.account.follows

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import ayush.ggv.instau.model.Post
import ayush.ggv.instau.presentation.common.EmptyScreen
import ayush.ggv.instau.presentation.components.ShimmerEffect
import ayush.ggv.instau.presentation.components.ShimmerFollowsListItemPlaceholder
import ayush.ggv.instau.presentation.components.ShimmerPostListItemPlaceholder
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import instaU.ayush.com.model.FollowUserData

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FollowsScreen(
    modifier: Modifier = Modifier,
    uiState: FollowsUiState,
    fetchFollows: () -> Unit,
    onItemClick: (Long) -> Unit,
    isFollowers: Boolean
) {

    val followListUsers = uiState.followUsers?.collectAsLazyPagingItems()
    val followingListUsers = uiState.followingUsers?.collectAsLazyPagingItems()

    val listUsers = if (isFollowers) followListUsers else followingListUsers
    Log.d("FollowersPagingSource" , "$listUsers")
    val isDataAvailable = listUsers?.let { handlePagingResult(it) } ?: false
    if (!isDataAvailable) {
        return
    }


    // Open Image in dialog
    var showDialog by remember { mutableStateOf(false) }
    var imageUrl by remember { mutableStateOf("") }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = listUsers?.loadState?.refresh == LoadState.Loading,
        onRefresh = {
            listUsers?.refresh()
        }
    )
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        listUsers?.let { lazyList ->
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
            ) {
                items(
                    items = lazyList,
                    key = { user -> user.id }
                ) { user ->
                    user?.let {
                        FollowsListItem(
                            name = it.name,
                            bio = it.bio,
                            imageUrl = it.imageUrl!!,
                            onItemClick = { onItemClick(it.id) },
                            onImageClick = {
                                imageUrl = it.imageUrl
                                showDialog = true
                            }
                        )
                    }
                }
            }

        }
        PullRefreshIndicator(
            refreshing =  listUsers?.loadState?.refresh == LoadState.Loading,
            state = pullRefreshState,
            modifier = modifier.align(Alignment.TopCenter)
        )

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
                    .size(300.dp)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(16.dp)) // Change the shape of the dialog box
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun handlePagingResult(
    followListUsers: LazyPagingItems<FollowUserData>,
): Boolean {
    followListUsers.apply {
        val error = when {
            loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
            loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
            loadState.append is LoadState.Error -> loadState.append as LoadState.Error
            else -> null
        }
        return when {
            loadState.refresh is LoadState.Loading -> {
                ShimmerEffect()
                false
            }

            error != null -> {
                EmptyScreen(error = error, followListUsers = followListUsers)
                false
            }

            followListUsers.itemCount < 1 -> {
                EmptyScreen()
                false
            }

            else -> true // Success
        }
    }
}
