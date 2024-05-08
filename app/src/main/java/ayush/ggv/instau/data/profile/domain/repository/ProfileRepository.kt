package ayush.ggv.instau.data.profile.domain.repository

import ayush.ggv.instau.data.profile.domain.model.ProfileResponse
import ayush.ggv.instau.model.PostResponse
import ayush.ggv.instau.model.PostTextParams
import ayush.ggv.instau.model.PostsResponse
import ayush.ggv.instau.util.Result

interface ProfileRepository {
    suspend fun getUserById(userId: Long , currentUserId : Long , token : String  ): Result<ProfileResponse>

}