package ayush.ggv.instau.presentation.screens.events

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.domain.usecases.eventsusecase.AddEventUseCase
import ayush.ggv.instau.domain.usecases.eventsusecase.GetEventsUseCase
import ayush.ggv.instau.model.events.Event
import ayush.ggv.instau.util.Result
import kotlinx.coroutines.launch

class EventsViewModel(
    private val eventsUseCase: GetEventsUseCase,
    private val addEventUseCase: AddEventUseCase
) : ViewModel() {
    private val _eventsUiState = mutableStateOf(EventsUiState())
    val qnaUiState: State<EventsUiState> = _eventsUiState

    init {
        loadEvents()
    }

    fun loadEvents() {
        viewModelScope.launch {
            _eventsUiState.value = _eventsUiState.value.copy(isLoading = true)
            when (val result = eventsUseCase()) {
                is Result.Success -> {
                    Log.d("EventsService", "getEventsVm: $result")
                    _eventsUiState.value = _eventsUiState.value.copy(
                        isLoading = false,
                        events = result.data?.events
                    )
                }

                is Result.Error -> {
                    Log.d("EventsService", "getEventsVmError: $result")
                    _eventsUiState.value = _eventsUiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }

                is Result.Loading -> {
                    _eventsUiState.value = _eventsUiState.value.copy(
                        isLoading = true
                    )
                }
            }

        }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            _eventsUiState.value = _eventsUiState.value.copy(isLoading = true)
            when (val result = addEventUseCase(event)) {
                is Result.Success -> {
                    Log.d("EventsService", "addEventVm: $result")
                    loadEvents()
                    _eventsUiState.value = _eventsUiState.value.copy(
                        isLoading = false
                    )
                }

                is Result.Error -> {
                    Log.d("EventsService", "addEventVmError: $result")
                    _eventsUiState.value = _eventsUiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }

                is Result.Loading -> {
                    _eventsUiState.value = _eventsUiState.value.copy(
                        isLoading = true
                    )
                }
            }
        }

    }
}


data class EventsUiState(
    val isLoading: Boolean = false,
    val events: List<Event>? = emptyList(),
    val errorMessage: String? = null
)