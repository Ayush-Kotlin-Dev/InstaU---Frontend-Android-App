package ayush.ggv.instau.model.qna

import ayush.ggv.instau.model.Post
import kotlinx.serialization.Serializable

@Serializable
data class QnaTextParams(
    val content: String,
    val userId: Long,
)

data class Question(
    val id: Long,
    val authorId: Long,
    val question: String,
    val createdAt: String,
)

@Serializable
data class QuestionWithAnswer(
    val id: Long,
    val authorId: Long,
    val authorName : String,
    val question: String,
    val createdAt: String,
    val mostRecentAnswer: String? = null
)

@Serializable
data class QuestionResponse(
    val success: Boolean,
    val questions: List<QuestionWithAnswer> = emptyList(),
    val message: String? = null
)

@Serializable
data class Answer(
    val id: Long,
    val questionId: Long,
    val authorId: Long,
    val answer: String,
    val createdAt: String,
)