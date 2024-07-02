package ayush.ggv.instau.data.profile.data

import ayush.ggv.instau.common.datastore.UserPreferences
import ayush.ggv.instau.data.posts.data.PostService
import ayush.ggv.instau.data.profile.domain.model.ProfileResponse
import ayush.ggv.instau.data.profile.domain.model.UpdateUserParams
import ayush.ggv.instau.data.profile.domain.repository.ProfileRepository
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.FollowUserData
import instaU.ayush.com.model.GetFollowsResponse

class ProfileRepositoryImpl(
    private val profileService: ProfileService,
    private val userPreferences: UserPreferences

) : ProfileRepository {

    override suspend fun getUserById(
        userId: Long,
    ): Result<ProfileResponse> {
        return try {
            val userData  = userPreferences.getUserData()
            val response = profileService.getUserProfile(userId, userData.id, userData.token)

            if (response.success) {
                Result.Success(response)
            } else {
                Result.Error(Exception("Error: ${response.message}").toString())
            }
        } catch (e: Exception) {
            Result.Error(e.toString())
        }

    }

    override suspend fun updateUserProfile(
        updateUserParams: UpdateUserParams,
    ): Result<ProfileResponse> {
        return try {
            val token = userPreferences.getUserData().token
            val response = profileService.updateUserProfile(updateUserParams, token)

            if (response.success) {
                Result.Success(response)
            } else {
                Result.Error(Exception("Error: ${response.message}").toString())
            }
        } catch (e: Exception) {
            Result.Error(e.toString())
        }

    }

    override suspend fun searchUsersByName(name: String, token: String): Result<GetFollowsResponse> {
        return try {
            val response = profileService.searchUsersByName(name, token)

            if(response.success){
                Result.Success(response)
            }else{
                Result.Error(Exception("Error: ${response.message}").toString())
            }
        } catch (e: Exception) {
            Result.Error(e.toString())
        }
    }
}