package ayush.ggv.instau.data.profile.domain.repository

import ayush.ggv.instau.data.profile.domain.model.ProfileResponse
import ayush.ggv.instau.data.profile.domain.model.UpdateUserParams
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.GetFollowsResponse

interface ProfileRepository {
    suspend fun getUserById(userId: Long ): Result<ProfileResponse>

    suspend fun updateUserProfile(image : ByteArray, updateUserParams: UpdateUserParams): Result<ProfileResponse>

    suspend fun searchUsersByName(name: String): Result<GetFollowsResponse>

}