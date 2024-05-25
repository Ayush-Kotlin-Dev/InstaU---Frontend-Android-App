package ayush.ggv.instau.domain.usecases.followsusecase

import ayush.ggv.instau.data.followunfollow.domain.FollowRepository
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.FollowsAndUnfollowsResponse
import instaU.ayush.com.model.FollowsParams
import instaU.ayush.com.model.GetFollowsResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetFollowersUseCase : KoinComponent {
    val repository: FollowRepository by inject()

    suspend operator fun invoke(
        userId: Long,
        pageNumber: Int,
        pageSize: Int,
        token: String
    ): Result<GetFollowsResponse> {
        return repository.getFollowers(userId, pageNumber, pageSize, token)
    }
}
