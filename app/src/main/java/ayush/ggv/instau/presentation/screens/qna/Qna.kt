package ayush.ggv.instau.presentation.screens.qna

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import ayush.ggv.instau.model.qna.QuestionWithAnswer
import ayush.ggv.instau.presentation.screens.destinations.QnaDetailDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun Qna(
    navigator: DestinationsNavigator,
    currentUserId: Long,
    token: String,
) {
    Log.d("Qna", "Qna: $token")
    val viewModel: QnaViewModel = koinViewModel()
    val qnaUiState = viewModel.qnaUiState.value
    LaunchedEffect(key1 = Unit ) {
        viewModel.fetchQuestionsWithAnswers(token)
    }
    QnaScreen(
        qnaUiState = qnaUiState,
        onQuestionClick = { questionWithAnswer ->
            navigator.navigate(
                QnaDetailDestination(
                    questionWithAnswer = questionWithAnswer,
                    currentUserId = currentUserId,
                    token = token
                )
            )
        }
    )


}