package ayush.ggv.instau.data.events.domain

import ayush.ggv.instau.model.events.EventResponse
import ayush.ggv.instau.model.events.EventsListResponse
import ayush.ggv.instau.util.Result

interface EventsRepository {
    suspend fun createEvent(
        event: ayush.ggv.instau.model.events.Event
    ): Result<EventResponse>

    suspend fun getEvents(
    ): Result<EventsListResponse>

    suspend fun getEvent(
        eventId: Long
    ): Result<EventResponse>

    suspend fun deleteEvent(
        eventId: Long
    ): Result<EventResponse>
}