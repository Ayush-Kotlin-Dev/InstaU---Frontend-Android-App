package ayush.ggv.instau.domain.usecases.postlikeusecase

import ayush.ggv.instau.data.postlike.domain.repository.PostLikesRepository
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.LikeParams
import instaU.ayush.com.model.LikeResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PostUnLikeUseCase : KoinComponent {

    private val repository: PostLikesRepository by inject()
    suspend operator fun invoke(
        likeParams: LikeParams,
        token : String
    ) : Result<LikeResponse> {
        return repository.removeLike(likeParams ,token)
    }
}
