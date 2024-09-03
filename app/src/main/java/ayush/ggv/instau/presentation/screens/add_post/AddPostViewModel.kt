package ayush.ggv.instau.presentation.screens.add_post

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import ayush.ggv.instau.domain.usecases.postsusecase.AddPostUseCase
import ayush.ggv.instau.model.PostParams
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class AddPostViewModel(
    private val addPostUseCase: AddPostUseCase,
    private val context: Context
) : ViewModel() {

    var uiState by mutableStateOf(AddPostUiState())
        private set


    var captionTextFieldValue: TextFieldValue by mutableStateOf(TextFieldValue())
        private set

    fun onCaptionChange(inputCaption: String) {
        captionTextFieldValue = captionTextFieldValue.copy(
            text = inputCaption,
            selection = captionTextFieldValue.selection
        )
    }

    fun onUploadPost(imageUri: Uri, caption: String) {
        viewModelScope.launch {
            if (caption.isEmpty()) {
                uiState = uiState.copy(error = "Caption cannot be empty")
                return@launch
            }

            uiState = uiState.copy(isLoading = true)

            try {
                val imagePath = saveImageToTempFile(imageUri)
                scheduleUploadWork(imagePath, caption)
                uiState = uiState.copy(isLoading = false, uploadScheduled = true)
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, error = "Failed to schedule upload: ${e.message}")
            }
        }
    }

    private fun saveImageToTempFile(imageUri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
        FileOutputStream(tempFile).use { outputStream ->
            inputStream?.copyTo(outputStream)
        }
        return tempFile.absolutePath
    }

    private fun scheduleUploadWork(imagePath: String, caption: String) {
        val uploadWorkRequest = UploadPostWorker.buildWorkRequest(imagePath, caption)
        WorkManager.getInstance(context).enqueue(uploadWorkRequest)
    }

    data class AddPostUiState(
        val isLoading: Boolean = false,
        val uploadScheduled: Boolean = false,
        val error: String? = null
    )
}