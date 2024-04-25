package ayush.ggv.instau.presentation.home

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
@Destination(start = true)

fun Home (
    navigator : DestinationsNavigator
){
    val viewModel : HomeScreenViewModel = koinViewModel()

    HomeScreen(
        onBoardingUiState = viewModel.onBoardingUiState,
        postsUiState = viewModel.postsUiState,
        onPostClick = { post ->

        },
        onProfileClick = { userId ->

        },
        onLikeClick = { postId ->

        },
        onCommentClick = { postId ->

        },
        onBoardingFinish = {

        },
        onUserClick = { user ->

        },
        onFollowClick = { isFollowing, user ->

        },
        fetchData = {
            viewModel.fetchData()
        }
    )
}