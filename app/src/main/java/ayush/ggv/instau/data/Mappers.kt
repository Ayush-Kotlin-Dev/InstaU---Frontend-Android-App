package ayush.ggv.instau.data

import ayush.ggv.instau.model.AuthResponseData
import ayush.ggv.instau.data.auth.domain.model.AuthResultData
import ayush.ggv.instau.model.FriendList
import ayush.ggv.instau.model.FriendListResponseDto
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
                username = it?.friendInfo?.username.orEmpty(),
                email = it?.friendInfo?.email.orEmpty(),
                avatar = it?.friendInfo?.avatar.orEmpty(),
                lastMessage = FriendList.FriendInfo.LastMessage(
                    textMessage = it?.friendInfo?.lastMessage?.textMessage,
                    timestamp = it?.friendInfo?.lastMessage?.timestamp
                )
            )
        )
    }
    return FriendList(
        friendInfo = friendList,
        errorMessage = error?.message
    )
}

private fun dateTimeFormat(timestamp: Long?): Pair<String, String> {
    val date = Date(timestamp ?: 0L)
    val formattedDate = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).format(date)
    val formattedTime = SimpleDateFormat("hh:mm aa", Locale.ENGLISH).format(date)
    return Pair(formattedDate, formattedTime)
}