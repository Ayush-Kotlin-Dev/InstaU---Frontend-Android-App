package ayush.ggv.instau.presentation.screens.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import ayush.ggv.instau.domain.usecases.eventsusecase.AddEventUseCase
import ayush.ggv.instau.domain.usecases.eventsusecase.GetEventsUseCase
import ayush.ggv.instau.model.events.Event
import ayush.ggv.instau.paging.PaginationManager
import ayush.ggv.instau.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventsViewModel(
    private val eventsUseCase: GetEventsUseCase,
    private val addEventUseCase: AddEventUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadEvents()
    }

    fun loadEvents() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val eventsPagingFlow = PaginationManager.createPagingFlow(
                fetcher = { page, pageSize ->
                    when (val result = eventsUseCase(page, pageSize)) {
                        is Result.Success -> {
                            result.data?.events ?: emptyList()
                        }
                        is Result.Error -> {
                            _uiState.value = _uiState.value.copy(errorMessage = result.message)
                            emptyList()
                        }
                        else -> emptyList()
                    }
                }
            ).flow.cachedIn(viewModelScope)

            _uiState.value = _uiState.value.copy(events = eventsPagingFlow, isLoading = false)
        }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isAddingEvent = true)
            when (val result = addEventUseCase(event)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isAddingEvent = false,
                        addEventSuccess = true
                    )
                    loadEvents() // Refresh events after adding a new one
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isAddingEvent = false,
                        errorMessage = result.message
                    )
                }
                is Result.Loading -> {
                    // This state is handled by setting isAddingEvent to true above
                }
            }
        }
    }

    fun onAddEventDialogDismiss() {
        _uiState.value = _uiState.value.copy(addEventSuccess = false)
    }
}

data class EventsUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val events: Flow<PagingData<Event>>? = null,
    val isAddingEvent: Boolean = false,
    val addEventSuccess: Boolean = false,
)