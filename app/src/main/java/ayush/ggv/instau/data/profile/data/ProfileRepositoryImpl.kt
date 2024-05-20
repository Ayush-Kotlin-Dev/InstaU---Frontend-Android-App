package ayush.ggv.instau.data.profile.data

import ayush.ggv.instau.data.posts.data.PostService
import ayush.ggv.instau.data.profile.domain.model.ProfileResponse
import ayush.ggv.instau.data.profile.domain.model.UpdateUserParams
import ayush.ggv.instau.data.profile.domain.repository.ProfileRepository
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.FollowUserData
import instaU.ayush.com.model.GetFollowsResponse

class ProfileRepositoryImpl(
    private val profileService: ProfileService
) : ProfileRepository {

    override suspend fun getUserById(
        userId: Long,
        currentUserId: Long,
        token: String
    ): Result<ProfileResponse> {
        return try {
            val response = profileService.getUserProfile(userId, currentUserId, token)

            if (response.success ==  true) {
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
        token: String
    ): Result<ProfileResponse> {
        return try {
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