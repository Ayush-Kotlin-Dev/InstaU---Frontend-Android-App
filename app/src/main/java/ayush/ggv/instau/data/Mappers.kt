package ayush.ggv.instau.data

import ayush.ggv.instau.model.AuthResponseData
import ayush.ggv.instau.data.auth.domain.model.AuthResultData

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
