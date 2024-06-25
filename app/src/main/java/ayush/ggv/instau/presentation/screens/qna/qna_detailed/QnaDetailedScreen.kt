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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ayush.ggv.instau.R
import ayush.ggv.instau.data.dateTimeFormat
import ayush.ggv.instau.model.qna.Answer
import ayush.ggv.instau.presentation.components.AnswerBubble
import ayush.ggv.instau.presentation.components.FollowsButton


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun QnaDetailedPage(
    currentUserId: Long,
    question: String,
    askedBy: String,
    askedAt: String,
    answers: List<Answer>,
    fetchData: () -> Unit
) {
    var isDialogOpen by remember { mutableStateOf(false) }
    var newAnswerText by remember { mutableStateOf("") }

    LaunchedEffect(key1 = Unit) {
        fetchData()
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
                    style = MaterialTheme.typography.h6,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Asked by $askedBy",
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = " at $askedAt",
                    style = MaterialTheme.typography.caption
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            reverseLayout = true
        ) {
            val groupByDate = answers.groupBy { dateTimeFormat(it.createdAt).first }

            groupByDate.forEach { (date, answersByDate) ->

                items(answersByDate) { answer ->
                    val isSender = answer.authorId == currentUserId
                    AnswerBubble(
                        answer = answer,
                        isSender = isSender
                    )
                }
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
                        value = newAnswerText,
                        onValueChange = { newAnswerText = it },
                        label = { Text("Answer...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(onClick = { ; newAnswerText = ""; isDialogOpen = false }) {
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