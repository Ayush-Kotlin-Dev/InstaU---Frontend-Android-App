package ayush.ggv.instau.model.qna

import kotlinx.serialization.Serializable



@Serializable
data class Answer(
    val id: Long,
    val questionId: Long,
    val authorId: Long,
    val authorName: String? = null,
    val answer: String,
    val createdAt: String,
)
@Serializable
data class AnswersResponse(
    val success: Boolean,
    val answers: List<Answer> = listOf(),
    val message: String? = null
)
@Serializable
data class AnswerTextParams(
    val content: String,
    val authorId: Long,
    val questionId: Long
)
@Serializable
data class AnswerResponse(
    val success: Boolean,
    val answer: Answer? = null,
    val message: String? = null
)