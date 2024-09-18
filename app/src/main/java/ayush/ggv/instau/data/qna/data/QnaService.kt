package ayush.ggv.instau.data.qna.data

import android.util.Log
import ayush.ggv.instau.data.KtorApi
import ayush.ggv.instau.model.qna.AnswerResponse
import ayush.ggv.instau.model.qna.AnswerTextParams
import ayush.ggv.instau.model.qna.AnswersResponse
import ayush.ggv.instau.model.qna.QnaTextParams
import ayush.ggv.instau.model.qna.QuestionResponse
import ayush.ggv.instau.model.qna.QuestionsResponse
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class QnaService : KtorApi() {
    suspend fun getQuestions(
        page : Int,
        pageSize : Int,
        token: String
    ): QuestionsResponse {
        val response = client.get {
            endPoint(path = "/qna/questions")
            headers {
                append("Authorization", "Bearer $token")
            }
            parameter("page", page)
            parameter("limit", pageSize)
        }
        return response.body<QuestionsResponse>()
    }
    suspend fun addQuestion(
        token: String,
        qnaTextParams: QnaTextParams
    ): QuestionResponse {
        val response = client.post {
            endPoint(path = "/qna/create")
            headers {
                append("Authorization", "Bearer $token")
                setBody(qnaTextParams)
            }
        }
        Log.d("QnaService", "addQuestion: ${response.body<QuestionResponse>()}")
            return response.body<QuestionResponse>()
    }

    suspend fun deleteQuestion(
        token: String,
        questionId: Long
    ): QuestionResponse {
        val response = client.delete {
            endPoint(path = "/qna/question/$questionId")
            headers {
                append("Authorization", "Bearer $token")
            }
        }
        return response.body<QuestionResponse>()
    }
    suspend fun getAnswers(
        token: String,
        questionId: Long,
        page: Int,
        limit: Int
    ): AnswersResponse {
        val response = client.get {
            endPoint(path = "/qna/answers")
            parameter("questionId", questionId)
            headers {
                append("Authorization", "Bearer $token")
            }
            parameter("page", page)
            parameter("limit", limit)
        }
        return response.body<AnswersResponse>()
    }

    suspend fun addAnswer(
        token: String,
        answerTextParams: AnswerTextParams
    ): AnswersResponse {
        val response = client.post {
            endPoint(path = "/qna/answer/create")
            headers {
                append("Authorization", "Bearer $token")
                setBody(answerTextParams)
            }
        }
        return response.body<AnswersResponse>()
    }

    suspend fun deleteAnswer(
        token: String,
        answerId: Long
    ): AnswerResponse {
        val response = client.delete {
            endPoint(path = "/qna/answer/$answerId")
            headers {
                append("Authorization", "Bearer $token")
            }
        }
        return response.body<AnswerResponse>()
    }
}