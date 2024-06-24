package ayush.ggv.instau.presentation.screens.qna

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import ayush.ggv.instau.presentation.components.QuestionItem


@Composable
fun QnaScreen(qnaUiState: QnaUiState) {
    val questionsWithAnswers = qnaUiState.questionsWithAnswers ?: emptyList()
    Log.d("QnaViewModel", "Qna: ${questionsWithAnswers.size}")
    Box(modifier = Modifier.fillMaxSize()) {
        if (qnaUiState.isLoading) {
            // Show loading indicator
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            // Show content when data is loaded
            if (questionsWithAnswers.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(questionsWithAnswers) { questionWithAnswer ->
                        QuestionItem(questionWithAnswer)
                        Divider()
                    }
                }
            }
        }
    }
}
