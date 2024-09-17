package ayush.ggv.instau.domain.usecases.qnausecase

import ayush.ggv.instau.data.qna.domain.QnaRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DeleteQuestionsUseCase  : KoinComponent {

    val repository: QnaRepository by inject()

    suspend operator fun invoke( questionId: Long) = repository.deleteQuestion(questionId)
}