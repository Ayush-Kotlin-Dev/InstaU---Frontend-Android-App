package ayush.ggv.instau.data.qna.data

import android.util.Log
import ayush.ggv.instau.common.datastore.UserPreferences
import ayush.ggv.instau.data.qna.domain.QnaRepository
import ayush.ggv.instau.model.qna.AnswerTextParams
import ayush.ggv.instau.model.qna.AnswersResponse
import ayush.ggv.instau.model.qna.QnaTextParams
import ayush.ggv.instau.model.qna.QuestionResponse
import ayush.ggv.instau.model.qna.QuestionsResponse
import ayush.ggv.instau.util.Result

class QnaRepositoryImpl(
    private val qnaService: QnaService,
    private val userPreferences: UserPreferences
) : QnaRepository {
    override suspend fun getQuestions(token: String): Result<QuestionsResponse> {
        return try {
            val userToken = userPreferences.getUserData().token

            val response = qnaService.getQuestions(userToken)

            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.toString())
        }
    }

    override suspend fun addQuestion(content: String): Result<QuestionResponse> {
        val userData = userPreferences.getUserData()
        return try {
            Log.d("QnaService", "addQuestion: $content")
            val response = qnaService.addQuestion(userData.token, QnaTextParams(content, userData.id))
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
            val userData = userPreferences.getUserData()
            val response = qnaService.getAnswers(userData.token, questionId, page, limit)

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
            val userData = userPreferences.getUserData()
            val response = qnaService.addAnswer(userData.token, answerTextParams = AnswerTextParams(content, userData.id ,questionId))

            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.toString())
        }
    }
}