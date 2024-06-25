package ayush.ggv.instau.presentation.screens.qna.qna_detailed

import androidx.compose.runtime.Composable
import ayush.ggv.instau.model.qna.Answer
import ayush.ggv.instau.model.qna.QuestionWithAnswer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel


@Destination
@Composable
fun QnaDetail(
    navigator: DestinationsNavigator,
    questionWithAnswer: QuestionWithAnswer,
    currentUserId: Long,
    token : String,
    ) {
    val viewModel: QnaDetailViewModel = koinViewModel()
    val answersUiState = viewModel.answersUiState.value

    QnaDetailedPage(
        currentUserId = currentUserId,
        question =questionWithAnswer.question,
        askedBy = questionWithAnswer.authorName,
        askedAt = questionWithAnswer.createdAt,
        answers = answersUiState.answers?: listOf(),
        fetchData = {
            viewModel.fetchQuestionsWithAnswers(token, questionWithAnswer.id)
        }
    )
}