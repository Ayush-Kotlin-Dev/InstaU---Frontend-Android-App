package ayush.ggv.instau.domain.usecases.profileusecase

import ayush.ggv.instau.data.profile.domain.repository.ProfileRepository
import ayush.ggv.instau.data.profile.domain.model.ProfileResponse
import ayush.ggv.instau.data.profile.domain.model.UpdateUserParams
import ayush.ggv.instau.util.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
class UpdateProfileUseCase : KoinComponent {
    private val repository: ProfileRepository by inject()

    suspend operator fun invoke(
        updateUserParams: UpdateUserParams,
    ): Result<ProfileResponse> {
        return repository.updateUserProfile(updateUserParams)
    }
}