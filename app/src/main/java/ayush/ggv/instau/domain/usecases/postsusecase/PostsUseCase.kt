package ayush.ggv.instau.domain.usecases.postusecase

import ayush.ggv.instau.data.posts.domain.repository.PostRepository
import ayush.ggv.instau.model.PostsResponse
import ayush.ggv.instau.util.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PostUseCase : KoinComponent {
    private val repository: PostRepository by inject()

    suspend operator fun invoke(
        currentUserId: Long,
        page: Int,
        limit: Int,
        token: String
    ): Result<PostsResponse> {
        return repository.getFeedPosts(currentUserId, page , limit, token)
    }
}