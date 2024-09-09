package ayush.ggv.instau.presentation.screens.events

import androidx.compose.runtime.Composable
import ayush.ggv.instau.presentation.screens.destinations.Destination
import org.koin.androidx.compose.koinViewModel


@Composable
@com.ramcosta.composedestinations.annotation.Destination
fun  Events(){
    val viewModel: EventsViewModel = koinViewModel()

    EventsScreen(viewModel)
}