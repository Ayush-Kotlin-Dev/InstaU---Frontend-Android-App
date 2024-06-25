package ayush.ggv.instau.presentation.screens.qna.qna_detailed

import androidx.compose.runtime.Composable
import ayush.ggv.instau.model.qna.Answer
import com.ramcosta.composedestinations.annotation.Destination


@Destination
@Composable
fun QnaDetail(

) {

    QnaDetailedPage(
        currentUserId = 1,
        question = "What is the best way to learn Jetpack Compose?",
        askedBy = "Ayush",
        askedAt = "12:00 PM",
        answers = listOf(
            Answer(
                id = 101,
                questionId = 1,
                authorId = 1,
                authorName = "Ayush",
                answer = "By building projects",
                createdAt = "12:00 PM"
            ),
            Answer(
                id = 101,
                questionId = 2,
                authorId = 1,
                authorName = "Ayush",
                answer = "By building projects",
                createdAt = "12:00 PM"
            ),
            Answer(
                id = 101,
                questionId = 1,
                authorId = 4,
                authorName = "Ayush",
                answer = "By building projects",
                createdAt = "12:00 PM"
            )
        )
    )
}