package ayush.ggv.instau.presentation.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ayush.ggv.instau.model.events.Event
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


@Composable
fun AddEventDialog(
    onDismiss: () -> Unit,
    onEventAdded: (Event) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var imageUrl by rememberSaveable { mutableStateOf("") }
    var organizer by rememberSaveable { mutableStateOf("") }
    var howToJoin by rememberSaveable { mutableStateOf("") }
    var additionalInfo by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var selectedDate by rememberSaveable { mutableStateOf(LocalDate.now()) }
    var selectedTime by rememberSaveable { mutableStateOf(LocalTime.now()) }

    var showErrors by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

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
        title = { Text(text = "Add New Event", style = MaterialTheme.typography.h6) },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)

            ){
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    item { EventTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Event Name",
                        isError = showErrors && name.isBlank()
                    ) }
                    item { EventTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = "Description",
                        isError = showErrors && description.isBlank()
                    ) }
                    item { EventTextField(
                        value = imageUrl,
                        onValueChange = { imageUrl = it },
                        label = "Image URL",
                        isError = showErrors && imageUrl.isBlank()
                    ) }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            DateTimeField(
                                value = selectedDate.format(dateFormatter),
                                label = "Date",
                                onClick = { showDatePicker() },
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            DateTimeField(
                                value = selectedTime.format(timeFormatter),
                                label = "Time",
                                onClick = { showTimePicker() },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    item { EventTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = "Location",
                        isError = showErrors && location.isBlank()
                    ) }
                    item { EventTextField(
                        value = organizer,
                        onValueChange = { organizer = it },
                        label = "Organizer",
                        isError = showErrors && organizer.isBlank()
                    ) }
                    item { EventTextField(
                        value = howToJoin,
                        onValueChange = { howToJoin = it },
                        label = "How to Join",
                        isError = showErrors && howToJoin.isBlank()
                    ) }
                    item { EventTextField(
                        value = additionalInfo,
                        onValueChange = { additionalInfo = it },
                        label = "Additional Info"
                    ) }
                    item {
                        AnimatedVisibility(visible = showErrors && !validateInputs()) {
                            Text(
                                "Please fill in all required fields",
                                color = MaterialTheme.colors.error,
                                style = MaterialTheme.typography.caption,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }

        },
        confirmButton = {
            Button(
                onClick = {
                    if (validateInputs()) {
                        val dateTime = selectedDate.atTime(selectedTime).atOffset(ZoneOffset.UTC).format(
                            DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                        val newEvent = Event(
                            name = name,
                            description = description,
                            imageUrl = imageUrl,
                            dateTime = dateTime,
                            organizer = organizer,
                            howToJoin = howToJoin,
                            additionalInfo = additionalInfo,
                            location = location
                        )
                        onEventAdded(newEvent)
                        onDismiss()
                    } else {
                        showErrors = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Event")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun EventTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        isError = isError,
        singleLine = true
    )
}

@Composable
fun DateTimeField(
    value: String,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { },
        label = { Text(label) },
        modifier = modifier.clickable(onClick = onClick),
        enabled = false,
        singleLine = true
    )
}