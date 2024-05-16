package ayush.ggv.instau.data.followunfollow.domain

import ayush.ggv.instau.data.profile.domain.model.ProfileResponse
import ayush.ggv.instau.data.profile.domain.model.UpdateUserParams
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.FollowsAndUnfollowsResponse
import instaU.ayush.com.model.FollowsParams
import instaU.ayush.com.model.GetFollowsResponse

interface FollowRepository {

    suspend fun followUser(
        followsParams: FollowsParams,
        token: String
    ): Result<FollowsAndUnfollowsResponse>

    suspend fun getFollowers(
        userId: Long,
        pageNumber: Int,
        pageSize: Int,
        token : String
    ): Result<GetFollowsResponse>
    suspend fun getFollowing(
        userId: Long,
        pageNumber: Int,
        pageSize: Int,
        token : String
    ): Result<GetFollowsResponse>


}