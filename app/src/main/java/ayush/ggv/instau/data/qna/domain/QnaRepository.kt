package ayush.ggv.instau.data.qna.domain

import ayush.ggv.instau.data.qna.data.QnaService
import ayush.ggv.instau.model.qna.QuestionResponse
import ayush.ggv.instau.util.Result

interface QnaRepository {
    suspend fun getQuestions(token: String): Result<QuestionResponse>
}