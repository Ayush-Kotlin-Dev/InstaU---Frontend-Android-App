package ayush.ggv.instau.presentation.components

import androidx.compose.material.Text
import androidx.datastore.core.DataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.common.datastore.UserSettings
import ayush.ggv.instau.common.datastore.toAuthResultData
import ayush.ggv.instau.domain.usecases.postlikeusecase.PostLikeUseCase
import ayush.ggv.instau.domain.usecases.postsusecase.DeletePostUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ayush.ggv.instau.util.Result
import com.google.firebase.storage.FirebaseStorage
import instaU.ayush.com.model.LikeParams
import instaU.ayush.com.model.LikeResponse

class PostListItemViewModel(
    private val dataStore: DataStore<UserSettings>,
    private val deletePostUseCase: DeletePostUseCase,
    private val postLikeUseCase: PostLikeUseCase

) : ViewModel() {

    val token = dataStore.data.map { it.toAuthResultData().token }
    var tokenString: String? = null
    val firebaseStorage = FirebaseStorage.getInstance()
    val likeResult = MutableLiveData<Result<LikeResponse>>()
    val isPostLiked = MutableLiveData<Boolean>()
    val likesCount = MutableLiveData<Int>()

    init {
        viewModelScope.launch {
            token.collect { value ->
                tokenString = value
            }
        }
    }

    fun deletePost(postId: Long, imageUrl: String) {
        viewModelScope.launch {
            deletePostUseCase(postId, tokenString!!)
            // Delete the image from Firebase Storage
            val imageRef = firebaseStorage.getReferenceFromUrl(imageUrl)
            imageRef.delete().addOnSuccessListener {
                // Image deleted successfully
            }.addOnFailureListener {
                // Failed to delete the image
            }
        }
    }
    fun likePost(likeParams: LikeParams , token : String) {
        viewModelScope.launch {
            val result = postLikeUseCase(likeParams, token)
            likeResult.value = result
            if (result is Result.Success) {
                isPostLiked.value = result.data?.success
                if (result.data?.success == true) {
                    likesCount.value = (likesCount.value ?: 0) + 1
                } else {
                    likesCount.value = (likesCount.value ?: 0) - 1
                }
            }
        }
    }

}
