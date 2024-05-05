package ayush.ggv.instau.domain.usecases.postsusecase

import ayush.ggv.instau.data.posts.domain.repository.PostRepository
import ayush.ggv.instau.model.PostResponse
import ayush.ggv.instau.model.PostTextParams
import ayush.ggv.instau.model.PostsResponse
import ayush.ggv.instau.util.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddPostUseCase : KoinComponent {
    private val repository: PostRepository by inject()

    suspend operator fun invoke(
       postTextParams: PostTextParams,
        token: String

    ): Result<PostResponse> {
        return repository.createPost(postTextParams, token)
    }
}