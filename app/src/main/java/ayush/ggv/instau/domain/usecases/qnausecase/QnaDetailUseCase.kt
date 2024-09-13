package ayush.ggv.instau.domain.usecases.qnausecase

import ayush.ggv.instau.data.qna.domain.QnaRepository
import ayush.ggv.instau.model.qna.AnswersResponse
import ayush.ggv.instau.util.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetAnswersUseCase : KoinComponent {
    private val repository: QnaRepository by inject()

    suspend operator fun invoke(
        token: String,
        questionId : Long,
        page : Int,
        limit : Int
    ): Result<AnswersResponse> {
        return repository.getAnswers(token , questionId , page , limit)
    }
}