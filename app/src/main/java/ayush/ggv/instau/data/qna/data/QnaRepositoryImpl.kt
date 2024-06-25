package ayush.ggv.instau.data.qna.data

import ayush.ggv.instau.data.qna.domain.QnaRepository
import ayush.ggv.instau.model.qna.AnswerTextParams
import ayush.ggv.instau.model.qna.AnswersResponse
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

    override suspend fun getAnswers(
        token: String,
        questionId: Long,
        page: Int,
        limit: Int
    ): Result<AnswersResponse> {
        return try {
            val response = qnaService.getAnswers(token, questionId, page, limit)

            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.toString())
        }
    }

    override suspend fun addAnswer(
        token: String,
        currentUserId : Long,
        questionId: Long,
        content: String
    ): Result<AnswersResponse> {
        return try {
            val response = qnaService.addAnswer(token, answerTextParams = AnswerTextParams(content, currentUserId ,questionId))

            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.toString())
        }
    }
}