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
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import ayush.ggv.instau.model.qna.QuestionWithAnswer
import ayush.ggv.instau.presentation.components.QuestionItem


@Composable
fun QnaScreen(
    qnaUiState: QnaUiState,
    onQuestionClick: (QuestionWithAnswer) -> Unit,
    onQuestionAddClick: (String) -> Unit,
    questionText: String,
    onTextChange: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

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
                        QuestionItem(questionWithAnswer, onQuestionClick)
                        Divider()
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    showDialog = true
                },
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Question")
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = {
                    Text(text = "Add Question")
                },
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
                    Button(
                        onClick = {
                            onQuestionAddClick(questionText)
                            showDialog = false
                        }
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
