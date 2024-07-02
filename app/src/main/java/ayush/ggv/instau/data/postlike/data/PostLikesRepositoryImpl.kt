package ayush.ggv.instau.data.postlike.data

import ayush.ggv.instau.common.datastore.UserPreferences
import ayush.ggv.instau.data.postlike.domain.repository.PostLikesRepository
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.LikeParams
import instaU.ayush.com.model.LikeResponse

class PostLikesRepositoryImpl(
    private val postLikeService: PostLikeService,
    private val userPreferences: UserPreferences
) : PostLikesRepository {
    override suspend fun addLike(
        params: LikeParams
        ): Result<LikeResponse> {
        return try{
            val response = postLikeService.addLike(params, userPreferences.getUserData().token)
            if(response.success) {
                Result.Success(response)
            } else {
                Result.Error(Exception("Error: ${response.message}").toString())
            }
        } catch (e: Exception) {
            Result.Error(e.toString())
        }

    }

    override suspend fun removeLike(params: LikeParams): Result<LikeResponse> {
        return try{
            val response = postLikeService.removeLike(params, userPreferences.getUserData().token)
            if(response.success) {
                Result.Success(response)
            } else {
                Result.Error(Exception("Error: ${response.message}").toString())
            }
        } catch (e: Exception) {
            Result.Error(e.toString())
        }
    }
}