package ayush.ggv.instau.data.chat.domain

import ayush.ggv.instau.model.FriendListResponseDto
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.FollowsAndUnfollowsResponse
import instaU.ayush.com.model.FollowsParams

interface ChatRepository {
    suspend fun getFriendList(
        userId: Long,
        token: String
    ): Result<FriendListResponseDto>
}