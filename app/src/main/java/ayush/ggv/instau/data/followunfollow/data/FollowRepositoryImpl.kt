package ayush.ggv.instau.data.followunfollow.data

import ayush.ggv.instau.data.followunfollow.domain.FollowRepository
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.FollowsAndUnfollowsResponse
import instaU.ayush.com.model.FollowsParams

class FollowRepositoryImpl(
   private val followService: FollowService
): FollowRepository {
    override suspend fun followUser(
        followsParams: FollowsParams,
        token: String
    ): Result<FollowsAndUnfollowsResponse> {
        return try {
            val response = followService.followUser(followsParams, token)
            if (response.success) {
                Result.Success(response)
            } else {
                Result.Error(Exception("Error: ${response.message}").toString())
            }
        } catch (e: Exception) {
            Result.Error(e.toString())
        }

    }
}