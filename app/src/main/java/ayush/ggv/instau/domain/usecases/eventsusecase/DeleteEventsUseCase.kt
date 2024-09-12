package ayush.ggv.instau.domain.usecases.eventsusecase

import ayush.ggv.instau.data.events.domain.EventsRepository
import ayush.ggv.instau.model.events.Event
import ayush.ggv.instau.model.events.EventResponse
import ayush.ggv.instau.util.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DeleteEventsUseCase  : KoinComponent {

    private val repository : EventsRepository by inject()

    suspend operator fun invoke (
        event: Event
    ): Result<EventResponse> {
        return repository.deleteEvent(event.id?:0)
    }


}