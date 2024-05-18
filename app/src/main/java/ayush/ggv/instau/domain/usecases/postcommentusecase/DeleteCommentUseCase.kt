package ayush.ggv.instau.domain.usecases.postcommentusecase

import ayush.ggv.instau.data.postcomments.domain.PostCommentsRepository
import ayush.ggv.instau.model.CommentResponse
import ayush.ggv.instau.model.NewCommentParams
import ayush.ggv.instau.model.RemoveCommentParams
import ayush.ggv.instau.util.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DeleteCommentUseCase  : KoinComponent {
    private val repository: PostCommentsRepository by inject()
    suspend operator fun invoke(
        params  : RemoveCommentParams,
        token : String
    ) : Result<CommentResponse> {
        return repository.removeComment(params ,token)
    }
}