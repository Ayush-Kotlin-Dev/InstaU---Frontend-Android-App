package ayush.ggv.instau.data.qna.data

import ayush.ggv.instau.data.KtorApi
import ayush.ggv.instau.data.profile.domain.model.ProfileResponse
import ayush.ggv.instau.model.qna.QuestionResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter

class QnaService : KtorApi() {
    suspend fun getQuestions(
        token: String
    ): QuestionResponse {
        val response = client.get {
            endPoint(path = "/qna/questions")
            headers {
                append("Authorization", "Bearer $token")
            }
        }
        return response.body<QuestionResponse>()
    }
}