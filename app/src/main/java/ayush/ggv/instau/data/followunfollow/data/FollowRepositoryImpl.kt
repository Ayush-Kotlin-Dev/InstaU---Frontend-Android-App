package ayush.ggv.instau.data.followunfollow.data

import ayush.ggv.instau.data.followunfollow.domain.FollowRepository
import ayush.ggv.instau.data.profile.domain.model.ProfileResponse
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.FollowsAndUnfollowsResponse
import instaU.ayush.com.model.FollowsParams
import instaU.ayush.com.model.GetFollowsResponse

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

    override suspend fun getFollowers(
        userId: Long,
        pageNumber: Int,
        pageSize: Int,
        token: String
    ): Result<GetFollowsResponse> {
        return try {
            val response = followService.getFollowers(userId, pageNumber, pageSize, token)
            if (response.success) {
                Result.Success(response)
            } else {
                Result.Error(Exception("Error: ${response.message}").toString())
            }
        } catch (e: Exception) {
            Result.Error(e.toString())
        }
    }

    override suspend fun getFollowing(
        userId: Long,
        pageNumber: Int,
        pageSize: Int,
        token: String
    ): Result<GetFollowsResponse> {
        return try {
            val response = followService.getFollowing(userId, pageNumber, pageSize , token )
            if (response.success) {
                Result.Success(response)
            } else {
                Result.Error(Exception("Error: ${response.message}").toString())
            }
        } catch (e: Exception) {
            Result.Error(e.toString())
        }
    }

    override suspend fun getSuggestions(userId: Long, token: String): Result<GetFollowsResponse> {
        return try {
            val response = followService.getSuggestions(userId, token)
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