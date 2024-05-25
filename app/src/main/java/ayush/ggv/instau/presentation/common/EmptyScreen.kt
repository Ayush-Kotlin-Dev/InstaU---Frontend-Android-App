package ayush.ggv.instau.presentation.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import ayush.ggv.instau.R
import ayush.ggv.instau.model.Post
import ayush.ggv.instau.ui.theme.NETWORK_ERROR_HEIGHT
import instaU.ayush.com.model.FollowUserData

@Composable
fun EmptyScreen(
    error: LoadState.Error?= null,
    posts : LazyPagingItems<Post>?=null,
    followListUsers : LazyPagingItems<FollowUserData>?=null
) {
    var errorMessage by remember {
        mutableStateOf("Find Your Favourite Hero")
    }
    var icon by remember {
        mutableStateOf(R.drawable.search_document)
    }
    if (error != null) {
        errorMessage = parseErrorMessage(message = error.toString())
        icon = R.drawable.network_error
    }
    var startAnimation by remember {
        mutableStateOf(false)
    }
    val alphaAnim by animateFloatAsState(
        targetValue = if(startAnimation) ContentAlpha.disabled else 0f,
        animationSpec = tween(
            durationMillis = 1000
        )
    )
    LaunchedEffect (key1 = startAnimation){
        startAnimation = true
    }

    EmptyContent(
        icon = icon,
        errorMessage = errorMessage,
        alphaAnim = alphaAnim,
        posts = posts,
        error = error
    )
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EmptyContent(
    error: LoadState.Error? = null,
    icon: Int,
    errorMessage: String,
    alphaAnim: Float,
    posts: LazyPagingItems<Post>?
) {
    var isRefreshing by remember {
        mutableStateOf(false)
    }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            posts?.refresh()
        }
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(state = pullRefreshState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .size(NETWORK_ERROR_HEIGHT)
                    .alpha(alphaAnim),
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray
            )

            Text(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .alpha(alphaAnim),
                text = errorMessage,
                color = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
            )
        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter) // Correct alignment for top center
        )
    }
}


fun parseErrorMessage(message: String): String {
    return when {
        message.contains("404") -> "No data found"
        message.contains("401") -> "Unauthorized"
        message.contains("403") -> "Forbidden"
        message.contains("500") -> "Server error"
        message.contains("ConnectException") -> "Internet Unavailable"
        else -> "Something went wrong"
    }
}