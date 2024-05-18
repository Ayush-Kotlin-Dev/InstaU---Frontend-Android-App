package ayush.ggv.instau.domain.usecases.postcommentusecase


import ayush.ggv.instau.data.KtorApi
import ayush.ggv.instau.data.followunfollow.domain.FollowRepository
import ayush.ggv.instau.data.postcomments.domain.PostCommentsRepository
import ayush.ggv.instau.model.CommentResponse
import ayush.ggv.instau.model.NewCommentParams
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.FollowsAndUnfollowsResponse
import instaU.ayush.com.model.FollowsParams
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CommentUseCase : KoinComponent {

    private val repository: PostCommentsRepository by inject()
    suspend operator fun invoke(
        params  : NewCommentParams,
        token : String
    ) : Result<CommentResponse> {
        return repository.addComment(params ,token)
    }
}