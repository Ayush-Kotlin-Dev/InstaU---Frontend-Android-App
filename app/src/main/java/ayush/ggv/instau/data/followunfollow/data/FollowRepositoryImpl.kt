package ayush.ggv.instau.data.followunfollow.data

import androidx.datastore.core.DataStore
import ayush.ggv.instau.common.datastore.UserPreferences
import ayush.ggv.instau.common.datastore.UserSettings
import ayush.ggv.instau.data.followunfollow.domain.FollowRepository
import ayush.ggv.instau.data.profile.domain.model.ProfileResponse
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.FollowsAndUnfollowsResponse
import instaU.ayush.com.model.FollowsParams
import instaU.ayush.com.model.GetFollowsResponse

class FollowRepositoryImpl(
   private val followService: FollowService,
   private val userPreferences: UserPreferences
): FollowRepository {
    override suspend fun followUser(
        followsParams: FollowsParams,
    ): Result<FollowsAndUnfollowsResponse> {
        return try {
            val userData = userPreferences.getUserData()
            val response = followService.followUser(followsParams.copy(follower = userData.id ), userData.token)
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
    ): Result<GetFollowsResponse> {
        return try {
            val token = userPreferences.getUserData().token
            val response = followService.getFollowers(userId, pageNumber, pageSize , token)
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
        pageSize: Int
    ): Result<GetFollowsResponse> {
        return try {
            val token = userPreferences.getUserData().token
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

    override suspend fun getSuggestions(): Result<GetFollowsResponse> {
        return try {
            val userData = userPreferences.getUserData()
            val response = followService.getSuggestions( userData.id, userData.token)
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