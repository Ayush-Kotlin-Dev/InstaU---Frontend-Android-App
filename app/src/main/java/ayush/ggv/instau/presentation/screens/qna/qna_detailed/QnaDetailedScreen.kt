package ayush.ggv.instau.presentation.screens.qna.qna_detailed

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import ayush.ggv.instau.data.dateTimeFormat
import ayush.ggv.instau.presentation.components.AnswerBubble
import kotlinx.coroutines.delay

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
    addAnswer: AddAnswerUiState,
    onTextChange: (String) -> Unit,
    questionId: Long
) {
    var simulatedProgress by remember { mutableStateOf(0f) }
    val dateTime = dateTimeFormat(askedAt)
    val pullRefreshState = rememberPullRefreshState(answers.isLoading, { fetchData() })
    val answersFlow = answers.answers?.collectAsLazyPagingItems()

    LaunchedEffect(key1 = questionId) {
        if (answersFlow == null || answersFlow.itemCount == 0) {
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

    Box(modifier = Modifier.fillMaxSize().pullRefresh(pullRefreshState)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            QuestionCard(question, askedBy, dateTime.first, dateTime.second)

            Spacer(modifier = Modifier.height(16.dp))

            if (answers.isLoading || addAnswer.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = simulatedProgress,
                        modifier = Modifier.size(64.dp)
                    )
                }
            } else if (answersFlow == null || answersFlow.itemCount == 0) {
                EmptyAnswersPlaceholder(modifier = Modifier.weight(1f))
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    val groupByDate = answersFlow.itemSnapshotList.items
                        .groupBy { dateTimeFormat(it.createdAt).first }

                    groupByDate.forEach { (date, answersByDate) ->
                        stickyHeader {
                            DateHeader(date)
                        }
                        items(
                            items = answersByDate,
                            key = { it.id }
                        ) { answer ->
                            val isSender = answer.authorId == currentUserId
                            AnswerBubble(answer = answer, isSender = isSender)
                        }
                    }

                    when (answersFlow.loadState.append) {
                        is LoadState.Loading -> {
                            item { LoadingItem() }
                        }
                        is LoadState.Error -> {
                            item { ErrorItem((answersFlow.loadState.append as LoadState.Error).error) }
                        }
                        else -> {}
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnswerInput(
                answerText = addAnswer.answer,
                onTextChange = onTextChange,
                onSendClick = {
                    onAddAnswer(addAnswer.answer)
                    onTextChange("")
                }
            )
        }

        PullRefreshIndicator(answers.isLoading, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
fun QuestionCard(question: String, askedBy: String, date: String, time: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp)
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Asked by $askedBy",
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = "$date at $time",
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}

@Composable
fun DateHeader(date: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background.copy(alpha = 0.9f))
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date,
            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colors.onBackground
        )
    }
}

@Composable
fun EmptyAnswersPlaceholder(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colors.primary.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No answers yet",
            style = MaterialTheme.typography.h6
        )
        Text(
            text = "Be the first to answer this question!",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun AnswerInput(
    answerText: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = answerText,
            onValueChange = onTextChange,
            placeholder = { Text("Type your answer...") },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            maxLines = 3
        )
        IconButton(
            onClick = onSendClick,
            enabled = answerText.isNotBlank()
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send answer",
                tint = if (answerText.isNotBlank()) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorItem(error: Throwable) {
    Text(
        text = "Error: ${error.message}",
        color = MaterialTheme.colors.error,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}