package ayush.ggv.instau.domain.usecases.qnausecase

import ayush.ggv.instau.data.qna.domain.QnaRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddAnswerUseCase : KoinComponent {

        val repository: QnaRepository by inject()

        suspend operator fun invoke(
            token: String,
            currentUserId : Long,
            questionId: Long,
            content: String
        ) = repository.addAnswer(token, currentUserId, questionId, content)
}