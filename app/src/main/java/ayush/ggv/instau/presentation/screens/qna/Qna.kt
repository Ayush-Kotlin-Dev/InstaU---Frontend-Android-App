package ayush.ggv.instau.presentation.screens.qna

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun Qna() {

    val viewModel : QnaViewModel = koinViewModel()
    val qnaUiState = viewModel.qnaUiState.value
    QnaScreen(
        qnaUiState = qnaUiState
    )


}