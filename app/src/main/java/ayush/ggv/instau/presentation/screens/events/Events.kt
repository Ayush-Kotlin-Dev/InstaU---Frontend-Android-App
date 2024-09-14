package ayush.ggv.instau.presentation.screens.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.koin.androidx.compose.koinViewModel


@Composable
@com.ramcosta.composedestinations.annotation.Destination
fun  Events(){
    val viewModel: EventsViewModel = koinViewModel()

    EventsScreen(
        eventsUiState = viewModel.uiState.collectAsState().value,
        onRefresh = viewModel::loadEvents,
        onEventAddClick = viewModel::addEvent,
        onAddEventDialogDismiss = viewModel::onAddEventDialogDismiss
    )
}