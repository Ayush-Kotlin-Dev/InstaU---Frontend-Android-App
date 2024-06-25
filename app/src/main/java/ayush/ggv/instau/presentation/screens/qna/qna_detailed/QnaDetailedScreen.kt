package ayush.ggv.instau.presentation.screens.qna.qna_detailed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
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
import ayush.ggv.instau.model.qna.Answer
import ayush.ggv.instau.presentation.components.AnswerBubble
import ayush.ggv.instau.presentation.components.FollowsButton


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun QnaDetailedPage(
    currentUserId: Long,
    question: String,
    askedBy: String,
    askedAt: String,
    answers: List<Answer>
) {
    var isDialogOpen by remember { mutableStateOf(false) }
    var newAnswertext by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        OutlinedButton(
            modifier = Modifier.align(Alignment.Start),
            onClick = {},
            shape = RoundedCornerShape(8.dp),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = "Back button"
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
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
        // Answers Section
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            reverseLayout = true
        ) {
            items(answers) { answer ->
                val isSender = answer.authorId == currentUserId

                AnswerBubble(
                    answer = answer,
                    isSender = isSender
                )
            }
        }
        FollowsButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = R.string.addAnswer,
            onFollowButtonClick = { isDialogOpen = true }
        )


        // Add Comment dialog
        if (isDialogOpen) {
            AlertDialog(
                onDismissRequest = { isDialogOpen = false },
                title = { Text("Add a Answer") },
                text = {
                    TextField(
                        value = newAnswertext,
                        onValueChange = { newAnswertext = it },
                        label = { Text("Answer...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(onClick = { ; newAnswertext = ""; isDialogOpen = false }) {
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

@Preview(showBackground = true)
@Composable
fun PreviewQnaDetailedPage() {
    QnaDetailedPage(
        currentUserId = 1,
        question = "What is the best way to learn Jetpack Compose?",
        askedBy = "Ayush",
        askedAt = "12:00 PM",
        answers = listOf(
            Answer(
                id = 101,
                questionId = 1,
                authorId = 1,
                authorName = "Ayush",
                answer = "By building projects",
                createdAt = "12:00 PM"
            ),
            Answer(
                id = 101,
                questionId = 2,
                authorId = 1,
                authorName = "Ayush",
                answer = "By building projects",
                createdAt = "12:00 PM"
            ),
            Answer(
                id = 101,
                questionId = 1,
                authorId = 4,
                authorName = "Ayush",
                answer = "By building projects",
                createdAt = "12:00 PM"
            )
        )
    )
}