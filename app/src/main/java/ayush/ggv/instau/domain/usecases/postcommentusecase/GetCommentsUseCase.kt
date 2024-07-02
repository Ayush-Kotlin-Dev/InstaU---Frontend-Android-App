package ayush.ggv.instau.domain.usecases.postcommentusecase

import ayush.ggv.instau.data.postcomments.domain.PostCommentsRepository
import ayush.ggv.instau.model.GetCommentsResponse
import ayush.ggv.instau.util.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetCommentsUseCase : KoinComponent {

    private val repository: PostCommentsRepository by inject()
    suspend operator fun invoke(
        postId : Long,
        pageNumber : Int,
        pageSize : Int
    ) : Result<GetCommentsResponse> {
        return repository.getComments(postId , pageNumber , pageSize )
    }
}