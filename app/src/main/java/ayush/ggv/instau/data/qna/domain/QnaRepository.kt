package ayush.ggv.instau.data.qna.domain

import ayush.ggv.instau.model.qna.AnswersResponse
import ayush.ggv.instau.model.qna.QuestionResponse
import ayush.ggv.instau.model.qna.QuestionsResponse
import ayush.ggv.instau.util.Result

interface QnaRepository {
    suspend fun getQuestions(): Result<QuestionsResponse>

    suspend fun addQuestion(content: String): Result<QuestionResponse>

    suspend fun getAnswers(token: String, questionId : Long , page : Int , limit : Int ) : Result<AnswersResponse>

    suspend fun addAnswer(token: String, currentUserId : Long ,  questionId: Long, content: String): Result<AnswersResponse>
}