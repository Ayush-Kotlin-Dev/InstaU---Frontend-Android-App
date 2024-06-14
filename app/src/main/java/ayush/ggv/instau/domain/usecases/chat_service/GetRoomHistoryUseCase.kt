package ayush.ggv.instau.domain.usecases.chat_service

import ayush.ggv.instau.data.chat.domain.ChatRepository
import ayush.ggv.instau.data.toFriendList
import ayush.ggv.instau.data.toRoomHistoryList
import ayush.ggv.instau.model.FriendList
import ayush.ggv.instau.model.friendList.RoomHistoryList
import ayush.ggv.instau.util.ResponseResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetRoomHistoryUseCase : KoinComponent {
    private val repository: ChatRepository by inject()

    suspend operator fun invoke(sender : Long , receiver: Long , token : String):
            Flow<ResponseResource<RoomHistoryList>> = flow {
        repository.getRoomHistory(sender,receiver,token).collect {
            val responseResource = when (it) {
                is ResponseResource.Error -> ResponseResource.error(it.errorMessage.toRoomHistoryList())
                is ResponseResource.Success -> ResponseResource.success(it.data.toRoomHistoryList())
            }
            emit(responseResource)
        }
    }

}