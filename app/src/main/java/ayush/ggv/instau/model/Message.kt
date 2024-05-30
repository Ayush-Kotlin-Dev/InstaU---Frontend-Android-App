package ayush.ggv.instau.model


data class Message(
    val user: User,
    val text: String,
    val imageUrl: String? = null,
)