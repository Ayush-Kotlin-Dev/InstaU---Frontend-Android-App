package ayush.ggv.instau.presentation.screens.account.edit

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
@Destination
fun EditProfile(
    userId: Long,
    navigator: DestinationsNavigator,

    ) {
    val viewModel: EditProfileViewModel = koinViewModel()

    EditProfileScreen(
        editProfileUiState = viewModel.uiState,
        onNameChange = viewModel::onNameChange,
        bioTextFieldValue = viewModel.bioTextFieldValue,
        onBioChange = viewModel::onBioChange,
        onUploadButtonClick = { imageByteArray ->
            viewModel.updateProfile( imageByteArray)
        },
        onUploadSuccess = { navigator.navigateUp()},
        fetchProfile = {
            viewModel.fetchProfile( userId )
        }
    )
}