package ayush.ggv.instau.model.qna




data class User(
    val id: Long,
    val name: String
)


data class AnswerWithUser(
    val answer: Answer,
    val answerer: User
)
