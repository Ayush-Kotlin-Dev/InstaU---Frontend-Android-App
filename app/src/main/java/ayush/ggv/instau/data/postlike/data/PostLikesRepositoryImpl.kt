package ayush.ggv.instau.data.postlike.data

import ayush.ggv.instau.data.postlike.domain.repository.PostLikesRepository
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.LikeParams
import instaU.ayush.com.model.LikeResponse

class PostLikesRepositoryImpl : PostLikesRepository {
    override suspend fun addLike(params: LikeParams): Result<LikeResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun removeLike(params: LikeParams): Result<LikeResponse> {
        TODO("Not yet implemented")
    }
}