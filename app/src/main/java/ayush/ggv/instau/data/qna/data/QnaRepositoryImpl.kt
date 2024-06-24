package ayush.ggv.instau.data.qna.data

import ayush.ggv.instau.data.qna.domain.QnaRepository
import ayush.ggv.instau.model.qna.QuestionResponse
import ayush.ggv.instau.util.Result

class QnaRepositoryImpl(
    private val qnaService: QnaService
) : QnaRepository {
    override suspend fun getQuestions(token: String): Result<QuestionResponse> {
        return try {
            val response = qnaService.getQuestions(token)

            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.toString())
        }
    }
}