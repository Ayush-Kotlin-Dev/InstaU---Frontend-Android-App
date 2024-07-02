package ayush.ggv.instau.domain.usecases.followsusecase

import ayush.ggv.instau.data.followunfollow.domain.FollowRepository
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.GetFollowsResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SuggestionsUseCase : KoinComponent {
    private val repository: FollowRepository by inject()

    suspend operator fun invoke(): Result<GetFollowsResponse> {
        return repository.getSuggestions()
    }
}
