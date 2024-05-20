package ayush.ggv.instau.domain.usecases.profileusecase

import androidx.room.Query
import ayush.ggv.instau.data.profile.domain.model.ProfileResponse
import ayush.ggv.instau.data.profile.domain.model.UpdateUserParams
import ayush.ggv.instau.data.profile.domain.repository.ProfileRepository
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.GetFollowsResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SearchUserUseCase : KoinComponent {
    private val repository: ProfileRepository by inject()
    suspend operator fun invoke(
        query: String ,
        token: String
    ): Result<GetFollowsResponse> {
        return repository.searchUsersByName(query, token)
    }
}