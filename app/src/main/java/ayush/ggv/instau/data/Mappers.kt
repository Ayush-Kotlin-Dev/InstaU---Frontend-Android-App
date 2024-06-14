package ayush.ggv.instau.data

import ayush.ggv.instau.model.AuthResponseData
import ayush.ggv.instau.data.auth.domain.model.AuthResultData
import ayush.ggv.instau.model.FriendList
import ayush.ggv.instau.model.FriendListResponseDto
import ayush.ggv.instau.model.chatRoom.ChatRoomResponseDto
import ayush.ggv.instau.model.chatRoom.MessageResponseDto
import ayush.ggv.instau.model.friendList.RoomHistoryList
import io.ktor.http.parsing.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun AuthResponseData.toAuthResultData() : AuthResultData{
   return AuthResultData(
         id = id,
         name = name,
         bio = bio,
         avatar = avatar,
         token = token,
         followersCount = followersCount,
         followingCount = followingCount
   )
}
fun FriendListResponseDto.toFriendList(): FriendList {
    val friendList = arrayListOf<FriendList.FriendInfo>()
    data?.forEach {
        friendList.add(
            FriendList.FriendInfo(
                username = it.friendInfo?.username.orEmpty(),
                friendId = it.friendInfo?.userId ?: 0L,
                avatar = it.friendInfo?.avatar.orEmpty(),
                lastMessage = FriendList.FriendInfo.LastMessage(
                    textMessage = it.friendInfo?.lastMessage?.textMessage,
                    timestamp = it.friendInfo?.lastMessage?.timestamp
                )
            )
        )
    }
    return FriendList(
        friendInfo = friendList,
        errorMessage = error?.message
    )
}

fun ChatRoomResponseDto.toRoomHistoryList(): RoomHistoryList {
    val roomHistoryList = arrayListOf<RoomHistoryList.Message>()
    data?.forEach {

        val (formattedDate, formattedTime) = dateTimeFormat(it?.timestamp)

        roomHistoryList.add(
            RoomHistoryList.Message(
                receiver = it?.receiver,
                sender = it?.sender,
                textMessage = it?.textMessage.orEmpty(),
                timestamp = it?.timestamp,
                formattedTime = formattedTime,
                formattedDate = formattedDate,
            )
        )
    }
    return RoomHistoryList(
        roomData = roomHistoryList,
        errorMessage = error?.message
    )
}

fun MessageResponseDto.toMessage(): RoomHistoryList.Message {

    val (formattedDate, formattedTime) = dateTimeFormat(timestamp)
    return RoomHistoryList.Message(
        sessionId = sessionId,
        textMessage = textMessage,
        sender = sender,
        receiver = receiver,
        timestamp = timestamp,
        formattedDate = formattedDate,
        formattedTime = formattedTime
    )
}


private fun dateTimeFormat(timestamp: String?): Pair<String, String> {
    return try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH)
        val date: Date = dateFormat.parse(timestamp ?: "") ?: Date(0L)
        val formattedDate = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).format(date)
        val formattedTime = SimpleDateFormat("hh:mm aa", Locale.ENGLISH).format(date)
        Pair(formattedDate, formattedTime)
    } catch (e: ParseException) {
        // Handle the case where the timestamp is not in the expected format
        Pair("Unknown Date", "Unknown Time")
    }
}
