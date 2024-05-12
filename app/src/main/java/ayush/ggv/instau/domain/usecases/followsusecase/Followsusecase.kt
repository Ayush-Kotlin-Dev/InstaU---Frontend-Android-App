package ayush.ggv.instau.domain.usecases.followsusecase

import ayush.ggv.instau.data.auth.domain.model.AuthResultData
import ayush.ggv.instau.data.auth.domain.repository.AuthRepository
import ayush.ggv.instau.data.followunfollow.domain.FollowRepository
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.FollowsAndUnfollowsResponse
import instaU.ayush.com.model.FollowsParams
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FollowsUseCase : KoinComponent{

    private val repository: FollowRepository by inject()
    suspend operator fun invoke(
        followsParams: FollowsParams,
        token : String
    ) : Result<FollowsAndUnfollowsResponse> {
        return repository.followUser(followsParams ,token)
    }
}
