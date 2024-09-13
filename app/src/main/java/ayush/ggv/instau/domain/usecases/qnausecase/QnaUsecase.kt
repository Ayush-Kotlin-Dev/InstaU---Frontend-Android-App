package ayush.ggv.instau.domain.usecases.qnausecase

import ayush.ggv.instau.data.qna.domain.QnaRepository
import ayush.ggv.instau.model.qna.QuestionsResponse
import ayush.ggv.instau.util.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class QnaUseCase : KoinComponent{
    private val repository: QnaRepository by inject()

    suspend operator fun invoke(
        page: Int,
        pageSize: Int
    ): Result<QuestionsResponse> {
        return repository.getQuestions(page, pageSize)
    }
}