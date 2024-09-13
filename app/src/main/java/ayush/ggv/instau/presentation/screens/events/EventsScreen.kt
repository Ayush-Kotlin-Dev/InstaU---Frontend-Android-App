package ayush.ggv.instau.presentation.screens.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ayush.ggv.instau.model.events.Event
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import androidx.paging.compose.items

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EventsScreen(
    eventsUiState: EventsUiState,
    onRefresh: () -> Unit,
    onEventAddClick: (Event) -> Unit,
    onAddEventDialogDismiss: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var simulatedProgress by remember { mutableStateOf(0f) }
    val events: LazyPagingItems<Event>? = eventsUiState.events?.collectAsLazyPagingItems()
    val pullRefreshState = rememberPullRefreshState(eventsUiState.isAddingEvent, onRefresh)

    LaunchedEffect(eventsUiState.isAddingEvent) {
        if (eventsUiState.isAddingEvent) {
            simulatedProgress = 0f
            while (simulatedProgress < 1f) {
                delay(100)
                simulatedProgress += 0.1f
            }
        }
    }

    LaunchedEffect(eventsUiState.addEventSuccess) {
        if (eventsUiState.addEventSuccess) {
            showDialog = false
            onAddEventDialogDismiss()
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .pullRefresh(pullRefreshState)
    ) {
        if (events != null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = events,
                    key = { event -> event.id.toString() }
                ) { event ->
                    event?.let { EventCard(event = it) }
                }

                when (events.loadState.append) {
                    is LoadState.Loading -> {
                        item { LoadingItem() }
                    }
                    is LoadState.Error -> {
                        item { ErrorItem((events.loadState.append as LoadState.Error).error) }
                    }
                    else -> {}
                }
            }
        }

        PullRefreshIndicator(eventsUiState.isAddingEvent, pullRefreshState, Modifier.align(Alignment.TopCenter))

        if (eventsUiState.isAddingEvent) {
            CircularProgressIndicator(
                progress = simulatedProgress,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        ExtendedFloatingActionButton(
            onClick = { showDialog = true },
            icon = { Icon(Icons.Filled.Add, contentDescription = "Add Event") },
            text = { Text("Add Event") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
        )

        if (showDialog) {
            AddEventDialog(
                onDismiss = { showDialog = false },
                onEventAdded = onEventAddClick
            )
        }

        eventsUiState.errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colors.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.BottomCenter)
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
fun EventCard(event: Event) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(model = event.imageUrl, contentDescription = null)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = event.name,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = event.description, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Date & Time: ${event.dateTime}", style = MaterialTheme.typography.body1)
            Text(text = "Organizer: ${event.organizer}", style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "How to Join:",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )
            Text(text = event.howToJoin, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Additional Info:",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )
            Text(text = event.additionalInfo, style = MaterialTheme.typography.body1)
        }
    }
}

// Keep the AddEventDialog as it was in the original code

@Composable
fun AddEventDialog(
    onDismiss: () -> Unit,
    onEventAdded: (Event) -> Unit // Pass event details back when added
) {
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var imageUrl by rememberSaveable { mutableStateOf("") }
    var dateTime by rememberSaveable { mutableStateOf("") }
    var organizer by rememberSaveable { mutableStateOf("") }
    var howToJoin by rememberSaveable { mutableStateOf("") }
    var additionalInfo by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add New Event") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Event Name") })
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") })
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("Image URL") })
                OutlinedTextField(
                    value = dateTime,
                    onValueChange = { dateTime = it },
                    label = { Text("Date and Time") })
                OutlinedTextField(
                    value = organizer,
                    onValueChange = { organizer = it },
                    label = { Text("Organizer") })
                OutlinedTextField(
                    value = howToJoin,
                    onValueChange = { howToJoin = it },
                    label = { Text("How to Join") })
                OutlinedTextField(
                    value = additionalInfo,
                    onValueChange = { additionalInfo = it },
                    label = { Text("Additional Info") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val newEvent = Event(
                    name = name,
                    description = description,
                    imageUrl = imageUrl,
                    dateTime = dateTime,
                    organizer = organizer,
                    howToJoin = howToJoin,
                    additionalInfo = additionalInfo
                )
                onEventAdded(newEvent)
                onDismiss()
            }) {
                Text("Add Event")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}