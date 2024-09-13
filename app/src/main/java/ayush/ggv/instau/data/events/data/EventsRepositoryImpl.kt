package ayush.ggv.instau.data.events.data

import android.util.Log
import ayush.ggv.instau.common.datastore.UserPreferences
import ayush.ggv.instau.data.events.domain.EventsRepository
import ayush.ggv.instau.model.events.Event
import ayush.ggv.instau.model.events.EventResponse
import ayush.ggv.instau.model.events.EventsListResponse
import ayush.ggv.instau.util.Result
import io.ktor.client.call.body

class EventsRepositoryImpl(
    private val eventsService: EventsService,
    private val userPreferences: UserPreferences
) : EventsRepository {


    override suspend fun createEvent(event: Event): Result<EventResponse> {
        return try {
            val token = userPreferences.getUserData().token
            val result = eventsService.createEvent(event, token)

            Result.Success(result)
        }catch (e:Exception){
            Result.Error(e.toString())
        }
    }

    override suspend fun getEvents(
        pageNumber: Int,
        pageSize: Int
    ): Result<EventsListResponse> {
        return try {
            val token = userPreferences.getUserData().token
            val result = eventsService.getEvents(pageNumber, pageSize, token)
            Log.d("EventsService", "getEventsREPO: $result")
            Result.Success(result)
        }catch (e : Exception){
            Log.d("EventsService", "getEventsREPOError: ${e.message}")
            Result.Error(e.toString())

        }

    }

    override suspend fun getEvent(eventId: Long): Result<EventResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEvent(eventId: Long): Result<EventResponse> {
        return try {
            val token = userPreferences.getUserData().token
            val result = eventsService.deleteEvent(eventId ,token)
            Result.Success(result)
        }catch (e : Exception){
            Result.Error(e.toString())
        }
    }
}