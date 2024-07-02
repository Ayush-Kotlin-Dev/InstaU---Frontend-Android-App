package ayush.ggv.instau.domain.usecases.postsusecase

import android.util.Log
import ayush.ggv.instau.data.posts.domain.repository.PostRepository
import ayush.ggv.instau.model.PostResponse
import ayush.ggv.instau.model.PostParams
import ayush.ggv.instau.util.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddPostUseCase : KoinComponent {
    private val repository: PostRepository by inject()

    suspend operator fun invoke(
        imageUri : ByteArray,
        postTextParams: PostParams
    ): Result<PostResponse> {
        Log.d("PostService", "createPost: Called USecase")

        return repository.createPost(imageUri ,postTextParams)
    }
}