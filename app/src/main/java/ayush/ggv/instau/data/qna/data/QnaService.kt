package ayush.ggv.instau.data.qna.data

import ayush.ggv.instau.data.KtorApi
import ayush.ggv.instau.model.qna.AnswerTextParams
import ayush.ggv.instau.model.qna.AnswersResponse
import ayush.ggv.instau.model.qna.QnaTextParams
import ayush.ggv.instau.model.qna.QuestionResponse
import ayush.ggv.instau.model.qna.QuestionsResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class QnaService : KtorApi() {
    suspend fun getQuestions(
        token: String
    ): QuestionsResponse {
        val response = client.get {
            endPoint(path = "/qna/questions")
            headers {
                append("Authorization", "Bearer $token")
            }
        }
        return response.body<QuestionsResponse>()
    }
    suspend fun addQuestion(
        token: String,
        qnaTextParams: QnaTextParams
    ): QuestionResponse {
        val response = client.post {
            endPoint(path = "/qna/question/create")
            headers {
                append("Authorization", "Bearer $token")
                setBody(qnaTextParams)
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


}