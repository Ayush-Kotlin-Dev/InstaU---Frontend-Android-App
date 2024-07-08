package ayush.ggv.instau.presentation.screens.qna

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import ayush.ggv.instau.data.dateTimeFormat
import ayush.ggv.instau.model.qna.QuestionWithAnswer
import ayush.ggv.instau.presentation.components.QuestionItem
import ayush.ggv.instau.presentation.screens.qna.qna_detailed.QnaDetailViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel


@Composable
fun QnaScreen(
    qnaUiState: QnaUiState,
    onQuestionClick: (QuestionWithAnswer) -> Unit,
    onQuestionAddClick: (String) -> Unit,
    questionText: String,
    onTextChange: (String) -> Unit
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var simulatedProgress by remember { mutableStateOf(0f) }
    LaunchedEffect(key1 = qnaUiState.isLoading) {
        if (qnaUiState.isLoading) {
            simulatedProgress = 0f
            while (simulatedProgress < 1f) {
                delay(100)
                simulatedProgress += 0.1f
            }
        }
    }
    val questionsWithAnswers = qnaUiState.questionsWithAnswers ?: emptyList()
    Log.d("QnaViewModel", "Qna: ${questionsWithAnswers.size}")
    Box(modifier = Modifier.fillMaxSize()) {
        if (qnaUiState.isLoading) {
            CircularProgressIndicator(
                progress = simulatedProgress,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            if (questionsWithAnswers.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(questionsWithAnswers) { questionWithAnswer ->
                        QuestionItem(questionWithAnswer, onQuestionClick)
                        Divider()
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    showDialog = true
                },
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Question")
            }
        }

        if (showDialog) {
            AddQuestionDialog(
                questionText = questionText,
                onTextChange = onTextChange,
                onAddClick = {
                    onQuestionAddClick(questionText)
                    showDialog = false
                },
                onCancelClick = {
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun AddQuestionDialog(
    questionText: String,
    onTextChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelClick,
        title = { Text(text = "Add Question") },
        text = {
            Column {
                TextField(
                    value = questionText,
                    onValueChange = onTextChange,
                    label = { Text("Question") }
                )
            }
        },
        confirmButton = {
            Button(onClick = onAddClick) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onCancelClick) {
                Text("Cancel")
            }
        }
    )
}