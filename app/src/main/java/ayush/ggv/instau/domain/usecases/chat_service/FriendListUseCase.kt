package ayush.ggv.instau.domain.usecases.chat_service

import ayush.ggv.instau.data.chat.domain.ChatRepository
import ayush.ggv.instau.data.toFriendList
import ayush.ggv.instau.model.FriendList
import ayush.ggv.instau.model.FriendListResponseDto
import ayush.ggv.instau.util.ResponseResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ayush.ggv.instau.util.Result

class FriendListUseCase : KoinComponent {
    private val repository: ChatRepository by inject()

    suspend operator fun invoke(
        userId: Long,
        token: String
    ): Flow<ResponseResource<FriendList>> = flow {
        repository.getFriendList( userId , token).collect {
            val responseResource = when (it) {
                is ResponseResource.Error -> ResponseResource.error(it.errorMessage.toFriendList())
                is ResponseResource.Success -> ResponseResource.success(it.data.toFriendList())
            }
            emit(responseResource)
        }
    }
}
