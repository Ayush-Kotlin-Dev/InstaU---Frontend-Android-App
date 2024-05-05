package ayush.ggv.instau.presentation.screens.add_post

import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.common.datastore.UserSettings
import ayush.ggv.instau.common.datastore.toAuthResultData
import ayush.ggv.instau.domain.usecases.postsusecase.AddPostUseCase
import ayush.ggv.instau.domain.usecases.postusecase.PostUseCase
import ayush.ggv.instau.model.PostTextParams
import ayush.ggv.instau.presentation.screens.account.edit.EditProfileUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ayush.ggv.instau.util.Result

class AddPostViewModel(
    private val addPostUseCase: AddPostUseCase,
) :ViewModel(

){


    var uiState by mutableStateOf((AddPostUiState()))
        private set


    var captionTextFieldValue : TextFieldValue by mutableStateOf(TextFieldValue())
        private set


    val  userId = mutableStateOf(-1L)

fun onCaptionChange(inputCaption : String){
        captionTextFieldValue = captionTextFieldValue.copy(
            text = inputCaption,
            selection = captionTextFieldValue.selection
        )
    }
    fun onUploadPost( imageUri: String, caption: String , token : String , userId : Long){

        viewModelScope.launch {
            uiState = uiState.copy(
                isLoading = true
            )
            val createPostResult = addPostUseCase(
                PostTextParams(
                    userId = userId,
                    caption = caption,
                    imageUrl = imageUri
                ),
                token = token
            )
            when(createPostResult){
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
        val isLoading : Boolean = false,
        val AddPost : PostTextParams? = null,
        val uploadSuccess : Boolean = false,
        val error : String? = null
    )


}