package ayush.ggv.instau.util

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.log

private const val SECOND_MILLIS = 1000L
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS

fun formatTimeAgo(time: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    val dateTime = LocalDateTime.parse(time, formatter)
    val zonedDateTime = dateTime.atZone(ZoneId.systemDefault())
    val now = ZonedDateTime.now()

    val diff = ChronoUnit.SECONDS.between(zonedDateTime, now)

    return when {
        diff < 60 -> "$diff seconds ago"
        diff < 3600 -> "${diff / 60} minutes ago"
        diff < 86400 -> "${diff / 3600} hours ago"
        diff < 604800 -> "${diff / 86400} days ago"
        else -> "${diff / 604800} weeks ago"
    }
}
