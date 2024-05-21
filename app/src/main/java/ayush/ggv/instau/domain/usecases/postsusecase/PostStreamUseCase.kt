package ayush.ggv.instau.domain.usecases.postsusecase

import androidx.paging.PagingData
import ayush.ggv.instau.data.posts.domain.repository.PostRepository
import ayush.ggv.instau.model.Post
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetPostsStreamUseCase : KoinComponent {
    private val repository: PostRepository by inject()

    operator fun invoke(): Flow<PagingData<Post>> {
        return repository.getPostsStream()
    }
}