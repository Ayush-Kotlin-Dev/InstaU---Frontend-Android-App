package ayush.ggv.instau.domain.usecases.postsusecase

import ayush.ggv.instau.data.posts.domain.repository.PostRepository
import ayush.ggv.instau.model.PostResponse
import ayush.ggv.instau.util.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DeletePostUseCase : KoinComponent {
    private val repository: PostRepository by inject()

    suspend operator fun invoke(
        postId: Long
    ): Result<PostResponse> {
        return repository.deletePost(postId)
    }
}