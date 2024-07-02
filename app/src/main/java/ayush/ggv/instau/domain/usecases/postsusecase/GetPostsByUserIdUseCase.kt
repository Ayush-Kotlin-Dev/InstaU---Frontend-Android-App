package ayush.ggv.instau.domain.usecases.postsusecase

import ayush.ggv.instau.data.posts.domain.repository.PostRepository
import ayush.ggv.instau.model.PostsResponse
import ayush.ggv.instau.util.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class getPostsByuserIdUseCase : KoinComponent {
    private val repository: PostRepository by inject()

    suspend operator fun invoke(
        userId : Long,
        page: Int,
        limit: Int,
    ): Result<PostsResponse> {
        return repository.getPostByUser(userId, page , limit)
    }
}