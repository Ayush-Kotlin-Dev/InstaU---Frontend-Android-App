package ayush.ggv.instau.data.followunfollow.domain

import ayush.ggv.instau.data.profile.domain.model.ProfileResponse
import ayush.ggv.instau.data.profile.domain.model.UpdateUserParams
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.FollowsAndUnfollowsResponse
import instaU.ayush.com.model.FollowsParams

interface FollowRepository {

    suspend fun followUser(followsParams : FollowsParams, token :String): Result<FollowsAndUnfollowsResponse>

}