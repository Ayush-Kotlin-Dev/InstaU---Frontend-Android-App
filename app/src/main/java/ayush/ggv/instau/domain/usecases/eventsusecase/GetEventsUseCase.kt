package ayush.ggv.instau.domain.usecases.eventsusecase

import android.util.Log
import ayush.ggv.instau.data.events.domain.EventsRepository
import ayush.ggv.instau.model.events.Event
import ayush.ggv.instau.model.events.EventResponse
import ayush.ggv.instau.model.events.EventsListResponse
import ayush.ggv.instau.model.events.EventsResponse
import ayush.ggv.instau.util.Result
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetEventsUseCase  : KoinComponent {

    private val repository : EventsRepository by inject()

    suspend operator fun invoke (
        pageNumber: Int,
        pageSize: Int,
    ): Result<EventsListResponse> {
        val result = repository.getEvents(pageNumber, pageSize)
        Log.d("EventsService", "getEventsUSECASE: $result")
        return result
    }
}