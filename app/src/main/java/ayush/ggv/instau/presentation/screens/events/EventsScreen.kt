package ayush.ggv.instau.presentation.screens.events

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ayush.ggv.instau.model.events.Event
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import androidx.paging.compose.items
import java.util.Calendar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

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

    LaunchedEffect(eventsUiState.isAddingEvent || eventsUiState.isLoading) {
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

        if (eventsUiState.isAddingEvent || eventsUiState.isLoading) {
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
    onEventAdded: (Event) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var organizer by remember { mutableStateOf("") }
    var howToJoin by remember { mutableStateOf("") }
    var additionalInfo by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
//    var tags by remember { mutableStateOf("") }

    var showErrors by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val scrollState = rememberScrollState()

    fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            },
            selectedDate.year,
            selectedDate.monthValue - 1,
            selectedDate.dayOfMonth
        )
        datePickerDialog.show()
    }

    fun showTimePicker() {
        val timePickerDialog = TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                selectedTime = LocalTime.of(hourOfDay, minute)
            },
            selectedTime.hour,
            selectedTime.minute,
            true
        )
        timePickerDialog.show()
    }

    fun validateInputs(): Boolean {
        return name.isNotBlank() && description.isNotBlank() && imageUrl.isNotBlank() &&
                organizer.isNotBlank() && howToJoin.isNotBlank() && location.isNotBlank()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add New Event") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Event Name") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = showErrors && name.isBlank()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = showErrors && description.isBlank()
                )
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("Image URL") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = showErrors && imageUrl.isBlank()
                )
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Event Image Preview",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = selectedDate.format(dateFormatter),
                        onValueChange = { },
                        label = { Text("Date") },
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showDatePicker() },
                        enabled = false
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = selectedTime.format(timeFormatter),
                        onValueChange = { },
                        label = { Text("Time") },
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showTimePicker() },
                        enabled = false
                    )
                }
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = showErrors && location.isBlank()
                )
                OutlinedTextField(
                    value = organizer,
                    onValueChange = { organizer = it },
                    label = { Text("Organizer") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = showErrors && organizer.isBlank()
                )
                OutlinedTextField(
                    value = howToJoin,
                    onValueChange = { howToJoin = it },
                    label = { Text("How to Join") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = showErrors && howToJoin.isBlank()
                )
//                OutlinedTextField(
//                    value = tags,
//                    onValueChange = { tags = it },
//                    label = { Text("Tags (comma-separated)") },
//                    modifier = Modifier.fillMaxWidth()
//                )
                OutlinedTextField(
                    value = additionalInfo,
                    onValueChange = { additionalInfo = it },
                    label = { Text("Additional Info") },
                    modifier = Modifier.fillMaxWidth()
                )
                AnimatedVisibility(visible = showErrors && !validateInputs()) {
                    Text(
                        "Please fill in all required fields",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (validateInputs()) {
                    val dateTime = selectedDate.atTime(selectedTime).atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                    val newEvent = Event(
                        name = name,
                        description = description,
                        imageUrl = imageUrl,
                        dateTime = dateTime,
                        organizer = organizer,
                        howToJoin = howToJoin,
                        additionalInfo = additionalInfo,
                        location = location,
//                        tags = tags.split(",").map { it.trim() }
                    )
                    onEventAdded(newEvent)
                    onDismiss()
                } else {
                    showErrors = true
                }
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