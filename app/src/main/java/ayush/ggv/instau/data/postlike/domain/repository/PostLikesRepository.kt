package ayush.ggv.instau.data.postlike.domain.repository

import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.LikeParams
import instaU.ayush.com.model.LikeResponse

interface PostLikesRepository {
    suspend fun addLike(params: LikeParams): Result<LikeResponse>

    suspend fun removeLike(params: LikeParams): Result<LikeResponse>
}