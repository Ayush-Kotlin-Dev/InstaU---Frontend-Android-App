package ayush.ggv.instau.presentation.screens.add_post

import androidx.compose.runtime.Composable
import ayush.ggv.instau.presentation.screens.destinations.HomeDestination
import ayush.ggv.instau.presentation.screens.home.HomeScreenViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
@Destination
fun AddPost(
    navigator: DestinationsNavigator,
    userId: Long?,
    token : String?
) {
    val viewModel: AddPostViewModel = koinViewModel()
    val homeScreenViewModel: HomeScreenViewModel = koinViewModel()
    AddPostScreen(
        addPostUiState = viewModel.uiState,
        captionText = viewModel.captionTextFieldValue.text,
        onCaptionChange = viewModel::onCaptionChange,
        onUploadPost = viewModel::onUploadPost,
        userId = userId!!,
        initialSelectedImageUri = "",
        onUploadSuccess = {
            homeScreenViewModel.fetchData()
            navigator.navigate(HomeDestination)
        },
        token = token!!
    )
}