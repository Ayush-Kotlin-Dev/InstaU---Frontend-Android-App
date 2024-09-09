package ayush.ggv.instau.presentation.screens.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Event(
    val name: String,
    val description: String,
    val imageResId: Int,
    val dateTime: String,
    val organizer: String,
    val howToJoin: String,
    val additionalInfo: String
)

class EventsViewModel : ViewModel() {
    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    init {
        loadEvents()
    }

    private fun loadEvents() {
        viewModelScope.launch {
            // In a real app, you'd fetch this data from a repository or API
            _events.value = listOf(
                Event(
                    name = "Annual Tech Fest",
                    description = "Join us for a day of cutting-edge technology demonstrations and workshops.",
                    imageResId = R.drawable.tech_fest_image,
                    dateTime = "2024-09-15 10:00 AM",
                    organizer = "Computer Science Department",
                    howToJoin = "Register online at techfest.university.edu",
                    additionalInfo = "Bring your laptop for hands-on sessions."
                ),
                Event(
                    name = "Cultural Night",
                    description = "Experience diverse cultures through music, dance, and food.",
                    imageResId = R.drawable.cultural_night_image,
                    dateTime = "2024-09-20 6:00 PM",
                    organizer = "International Students Association",
                    howToJoin = "Free entry for all students. Show your ID at the gate.",
                    additionalInfo = "Traditional attire is encouraged but not mandatory."
                )
                // Add more events as needed
            )
        }
    }
}

@Composable
fun EventsScreen(viewModel: EventsViewModel) {
    val events by viewModel.events.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(events) { event ->
            EventCard(event)
        }
    }
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
            Image(
                painter = painterResource(id = event.imageResId),
                contentDescription = "Event image for ${event.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = event.name, style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = event.description, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Date & Time: ${event.dateTime}", style = MaterialTheme.typography.body1)
            Text(text = "Organizer: ${event.organizer}", style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "How to Join:", style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold)
            Text(text = event.howToJoin, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Additional Info:", style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold)
            Text(text = event.additionalInfo, style = MaterialTheme.typography.body1)
        }
    }
}