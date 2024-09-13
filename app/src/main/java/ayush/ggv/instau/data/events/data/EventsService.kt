package ayush.ggv.instau.data.events.data

import android.util.Log
import ayush.ggv.instau.data.KtorApi
import ayush.ggv.instau.model.events.Event
import ayush.ggv.instau.model.events.EventResponse
import ayush.ggv.instau.model.events.EventsListResponse
import ayush.ggv.instau.model.events.EventsResponse
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class EventsService : KtorApi() {

    suspend fun createEvent(
         event: Event,
         token: String
    )  : EventResponse {
        val response = client.post {
            endPoint(path = "/events/create")
            headers {
                append("Authorization", "Bearer $token")

            }
            setBody(event)

        }
        return response.body<EventResponse>()
    }

    suspend fun getEvents(
        page : Int,
        limit : Int,
        token: String
    ): EventsListResponse {
        val response = client.get {
            endPoint(path = "/events/get")
            headers {
                append("Authorization", "Bearer $token")
            }
            parameter("page", page)
            parameter("limit", limit)
        }
        Log.d("EventsService", "getEventsService: ${response.body<EventsListResponse>()}")
        return response.body<EventsListResponse>()
    }

    suspend fun getEvent(
        eventId: Long,
        token: String
    ): EventResponse {
        val response = client.get {
            endPoint(path = "/events/delete/$eventId")
            headers {
                append("Authorization", "Bearer $token")
            }
        }
        return response.body<EventResponse>()
    }

    suspend fun deleteEvent(
        eventId: Long,
        token: String
    ): EventResponse {
        val response = client.delete {
            endPoint(path = "/events/delete/$eventId")
            headers {
                append("Authorization", "Bearer $token")
            }
        }
        return response.body<EventResponse>()
    }
}