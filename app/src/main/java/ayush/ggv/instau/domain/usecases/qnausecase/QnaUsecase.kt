package ayush.ggv.instau.domain.usecases.qnausecase

import ayush.ggv.instau.data.profile.domain.model.ProfileResponse
import ayush.ggv.instau.data.qna.domain.QnaRepository
import ayush.ggv.instau.model.qna.QuestionResponse
import ayush.ggv.instau.util.Result
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.inject
import org.koin.core.component.inject

class QnaUseCase : KoinComponent{
    private val repository: QnaRepository by inject()

    suspend operator fun invoke(
        token: String
    ): Result<QuestionResponse> {
        return repository.getQuestions(token)
    }
}