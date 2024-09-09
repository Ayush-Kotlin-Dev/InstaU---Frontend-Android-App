package ayush.ggv.instau.presentation.screens.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
                    imageResId = R.drawable.tech_fest_day,
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