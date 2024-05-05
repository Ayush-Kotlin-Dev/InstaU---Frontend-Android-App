package ayush.ggv.instau.presentation.screens.add_post

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ayush.ggv.instau.presentation.screens.account.edit.EditProfileScreen
import ayush.ggv.instau.presentation.screens.account.edit.EditProfileViewModel
import ayush.ggv.instau.presentation.screens.home.HomeScreenViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
@Destination()
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
        initialSelectedImageUri = viewModel.uiState.AddPost?.imageUrl,
        onUploadSuccess = {
            homeScreenViewModel.fetchData()
            navigator.navigateUp()

                          },
        token = token!!
    )
}