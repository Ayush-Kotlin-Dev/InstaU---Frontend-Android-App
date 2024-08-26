package ayush.ggv.instau.presentation.screens.add_post

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.domain.usecases.postsusecase.AddPostUseCase
import ayush.ggv.instau.model.PostParams
import ayush.ggv.instau.util.Result
import kotlinx.coroutines.launch

class AddPostViewModel(
    private val addPostUseCase: AddPostUseCase,
) : ViewModel(

) {


    var uiState by mutableStateOf((AddPostUiState()))
        private set


    var captionTextFieldValue: TextFieldValue by mutableStateOf(TextFieldValue())
        private set

    fun onCaptionChange(inputCaption: String) {
        captionTextFieldValue = captionTextFieldValue.copy(
            text = inputCaption,
            selection = captionTextFieldValue.selection
        )
    }

    fun onUploadPost(imageUri: ByteArray, caption: String) {

        viewModelScope.launch {
            //check caption is not empty
            if (caption.isEmpty()) {
                uiState = uiState.copy(
                    error = "Caption cannot be empty"
                )
                return@launch
            }
            uiState = uiState.copy(
                isLoading = true
            )


            val createPostResult = addPostUseCase(
                imageUri = imageUri,
                PostParams(
                    userId = null,
                    caption = caption,
                )
            )
            when (createPostResult) {
                is Result.Error -> {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = createPostResult.message
                    )
                }

                is Result.Success -> {
                    uiState = uiState.copy(
                        isLoading = false,
                        uploadSuccess = true
                    )
                }

                is Result.Loading ->
                    uiState = uiState.copy(
                        isLoading = true
                    )
            }
        }


    }

    data class AddPostUiState(
        val isLoading: Boolean = false,
        val AddPost: PostParams? = null,
        val uploadSuccess: Boolean = false,
        val error: String? = null
    )


}