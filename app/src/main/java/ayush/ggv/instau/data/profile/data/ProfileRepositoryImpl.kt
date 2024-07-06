package ayush.ggv.instau.data.profile.data

import android.util.Log
import ayush.ggv.instau.common.datastore.UserPreferences
import ayush.ggv.instau.common.datastore.UserSettings
import ayush.ggv.instau.data.posts.data.PostService
import ayush.ggv.instau.data.profile.domain.model.ProfileResponse
import ayush.ggv.instau.data.profile.domain.model.UpdateUserParams
import ayush.ggv.instau.data.profile.domain.model.toDomainProfile
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
            if(userData.id== userId){
                return Result.Success(ProfileResponse(true,userData.toDomainProfile()))
            }
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
                // Convert UpdateUserParams to UserSettings and save it
                val updatedUserSettings = UserSettings(
                    id = updateUserParams.userId,
                    name = updateUserParams.name,
                    bio = updateUserParams.bio,
                    avatar = updateUserParams.imageUrl,
                    followersCount = userPreferences.getUserData().followersCount, // Assuming these values remain unchanged
                    followingCount = userPreferences.getUserData().followingCount,
                    token = userPreferences.getUserData().token
                )
                userPreferences.setUserData(updatedUserSettings)
                Result.Success(response)
            } else {
                Result.Error(Exception("Error: ${response.message}").toString())
            }
        } catch (e: Exception) {
            Result.Error(e.toString())
        }
    }

    override suspend fun searchUsersByName(name: String): Result<GetFollowsResponse> {
        return try {
            val userToken = userPreferences.getUserData().token
            val response = profileService.searchUsersByName(name, userToken)

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