package ayush.ggv.instau.presentation.screens.qna.qna_detailed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
    token: String,
) {
    val viewModel: QnaDetailViewModel = koinViewModel()
    val answersUiState = viewModel.answersUiState.collectAsState().value

    // Extract questionId from questionWithAnswer
    val questionId = questionWithAnswer.id

    QnaDetailedPage(
        currentUserId = currentUserId,
        question = questionWithAnswer.question,
        askedBy = questionWithAnswer.authorName,
        askedAt = questionWithAnswer.createdAt,
        answers = answersUiState,
        fetchData = {
            viewModel.fetchAnswers(token, questionId)
        },
        onAddAnswer = { content ->
            viewModel.addAnswer(token, currentUserId, questionId, content)
        },
        addAnswer = viewModel.addAnswerUiState.collectAsState().value,
        onTextChange = viewModel::onTextChange,
        questionId = questionId // Pass questionId to QnaDetailedPage
    )
}
