package ayush.ggv.instau.presentation.screens.qna.qna_detailed

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ayush.ggv.instau.R
import ayush.ggv.instau.data.dateTimeFormat
import ayush.ggv.instau.presentation.components.AnswerBubble
import ayush.ggv.instau.presentation.components.FollowsButton
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun QnaDetailedPage(
    currentUserId: Long,
    question: String,
    askedBy: String,
    askedAt: String,
    answers: AnswersState,
    fetchData: () -> Unit,
    onAddAnswer: (String) -> Unit,
    addAnswer: addAnsweruiState,
    onTextChange: (String) -> Unit,
    questionId: Long // Add questionId as a parameter
) {
    var isDialogOpen by remember { mutableStateOf(false) }
    var simulatedProgress by remember { mutableStateOf(0f) }
    val dateTime = dateTimeFormat(askedAt)
    val viewModel: QnaDetailViewModel = koinViewModel()

    // Only fetch data if it hasn't been fetched yet
    LaunchedEffect(key1 = questionId, key2 = viewModel.answersUiState.value.answers) {
        if (viewModel.answersUiState.value.answers.isNullOrEmpty()) {
            fetchData()
        }
    }

    LaunchedEffect(key1 = answers.isLoading || addAnswer.isLoading) {
        if (answers.isLoading || addAnswer.isLoading) {
            simulatedProgress = 0f
            while (simulatedProgress < 1f) {
                delay(100)
                simulatedProgress += 0.1f
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = question,
                    style = MaterialTheme.typography.h6.copy(fontSize = 20.sp),
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Asked by $askedBy",
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = "at $dateTime",
                    style = MaterialTheme.typography.caption
                )
            }
        }

        if (answers.isLoading || addAnswer.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = simulatedProgress,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                val groupByDate =
                    answers.answers?.groupBy { dateTimeFormat(it.createdAt).first }

                groupByDate?.forEach { (date, answersByDate) ->
                    stickyHeader {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = date,
                                style = MaterialTheme.typography.body2
                                    .copy(color = MaterialTheme.colors.onBackground)
                            )
                        }
                    }
                    items(answersByDate) { answer ->
                        val isSender = answer.authorId == currentUserId
                        AnswerBubble(
                            answer = answer,
                            isSender = isSender
                        )
                    }

                }
            }
        }

        FollowsButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = R.string.addAnswer,
            onFollowButtonClick = { isDialogOpen = true }
        )

        if (isDialogOpen) {
            AlertDialog(
                onDismissRequest = { isDialogOpen = false },
                title = { Text("Add an Answer") },
                text = {
                    TextField(
                        value = addAnswer.answer,
                        onValueChange = { onTextChange(it) },
                        label = { Text("Answer...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onAddAnswer(addAnswer.answer)
                            onTextChange("")
                            isDialogOpen = false
                        },
                        enabled = (addAnswer.answer.isNotEmpty())
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    Button(onClick = { isDialogOpen = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
