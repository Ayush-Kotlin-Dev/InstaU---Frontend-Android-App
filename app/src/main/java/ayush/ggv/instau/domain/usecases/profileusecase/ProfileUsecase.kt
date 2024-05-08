package ayush.ggv.instau.domain.usecases.profileusecase

import ayush.ggv.instau.data.profile.domain.repository.ProfileRepository
import ayush.ggv.instau.data.profile.domain.model.ProfileResponse
import ayush.ggv.instau.util.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProfileUseCase : KoinComponent {
    private val repository: ProfileRepository by inject()

    suspend operator fun invoke(
        userId: Long,
        currentUserId: Long,
        token: String
    ): Result<ProfileResponse> {
        return repository.getUserById(userId, currentUserId, token)
    }
}