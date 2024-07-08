package ayush.ggv.instau.model.qna

import kotlinx.serialization.Serializable

@Serializable
data class QnaTextParams(
    val content: String,
    val userId: Long,
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
data class QuestionsResponse(
    val success: Boolean,
    val questions: List<QuestionWithAnswer> = emptyList(),
    val message: String? = null
)

@Serializable
data class QuestionResponse(
    val success: Boolean,
    val question: QuestionWithAnswer? = null,
    val message: String? = null
)