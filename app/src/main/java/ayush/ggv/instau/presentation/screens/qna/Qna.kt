package ayush.ggv.instau.presentation.screens.qna

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
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
    val qnaUiState by remember { viewModel.qnaUiState }
    val questionText by remember { viewModel.questionText }
    val context = LocalContext.current

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
        },
        onQuestionAddClick = { content ->
            viewModel.addQuestion(content)
        },
        questionText = questionText,
        onTextChange = viewModel::setQuestionText,
        onRefresh = { Toast.makeText(context, "Refreshed", Toast.LENGTH_SHORT).show() }
    )
}