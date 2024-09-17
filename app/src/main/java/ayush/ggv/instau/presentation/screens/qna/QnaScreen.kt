package ayush.ggv.instau.presentation.screens.qna

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun QnaScreen(
    qnaUiState: QnaUiState,
    onQuestionClick: (QuestionWithAnswer) -> Unit,
    onQuestionAddClick: (String) -> Unit,
    questionText: String,
    onTextChange: (String) -> Unit,
    onRefresh: () -> Unit,
    onDeleteQuestion: (Long) -> Unit,
    currentUserId: Long
) {
    var showDialog by remember { mutableStateOf(false) }
    var simulatedProgress by remember { mutableStateOf(0f) }
    val questionsWithAnswers = qnaUiState.questionsWithAnswers?.collectAsLazyPagingItems()
    val pullRefreshState = rememberPullRefreshState(qnaUiState.isLoading, onRefresh)

    LaunchedEffect(key1 = qnaUiState.isLoading) {
        if (qnaUiState.isLoading) {
            simulatedProgress = 0f
            while (simulatedProgress < 1f) {
                delay(100)
                simulatedProgress += 0.1f
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().pullRefresh(pullRefreshState)) {
        if (questionsWithAnswers != null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(
                    items = questionsWithAnswers,
                    key = { it.id }
                ) { questionWithAnswer ->
                    questionWithAnswer?.let {
                        QuestionItem(
                            it,
                            onQuestionClick,
                            onDeleteQuestion,
                            currentUserId
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                when (questionsWithAnswers.loadState.append) {
                    is LoadState.Loading -> {
                        item { LoadingItem() }
                    }
                    is LoadState.Error -> {
                        item { ErrorItem((questionsWithAnswers.loadState.append as LoadState.Error).error) }
                    }
                    else -> {}
                }
            }
        } else if (qnaUiState.isLoading) {
            CircularProgressIndicator(
                progress = simulatedProgress,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            EmptyQuestionsPlaceholder(modifier = Modifier.align(Alignment.Center))
        }

        PullRefreshIndicator(qnaUiState.isLoading, pullRefreshState, Modifier.align(Alignment.TopCenter))

        ExtendedFloatingActionButton(
            onClick = { showDialog = true },
            icon = { Icon(Icons.Default.Add, contentDescription = null) },
            text = { Text("Ask Question") },
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        )

        if (showDialog) {
            AddQuestionDialog(
                questionText = questionText,
                onTextChange = onTextChange,
                onAddClick = {
                    onQuestionAddClick(questionText)
                    showDialog = false
                },
                onCancelClick = { showDialog = false }
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

@Composable
fun EmptyQuestionsPlaceholder(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colors.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No questions yet",
            style = MaterialTheme.typography.h6
        )
        Text(
            text = "Be the first to ask a question!",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun AddQuestionDialog(
    questionText: String,
    onTextChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    val maxCharacters = 200
    val remainingCharacters = maxCharacters - questionText.length

    AlertDialog(
        onDismissRequest = onCancelClick,
        title = { Text(text = "Ask a Question") },
        text = {
            Column {
                OutlinedTextField(
                    value = questionText,
                    onValueChange = { if (it.length <= maxCharacters) onTextChange(it) },
                    label = { Text("Your Question") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "$remainingCharacters characters remaining",
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onAddClick,
                enabled = questionText.isNotBlank()
            ) {
                Text("Ask")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelClick) {
                Text("Cancel")
            }
        }
    )
}

