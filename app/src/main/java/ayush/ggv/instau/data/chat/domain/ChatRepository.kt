package ayush.ggv.instau.data.chat.domain

import ayush.ggv.instau.model.FriendListResponseDto
import ayush.ggv.instau.util.ResponseResource
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.FollowsAndUnfollowsResponse
import instaU.ayush.com.model.FollowsParams
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getFriendList(
        userId: Long,
        token: String
    ): Flow<ResponseResource<FriendListResponseDto>>
}