package instaU.ayush.com.model

import kotlinx.serialization.Serializable

@Serializable
data class FollowsAndUnfollowsResponse(
    val success: Boolean,
    val message: String? = null
)

@Serializable
data class FollowsParams(
    val follower : Long ? = null,
    val following : Long,
    val isFollowing : Boolean
)

@Serializable
data class FollowUserData(
    val id: Long,
    val name: String,
    val bio: String,
    val imageUrl: String? = null,
    val isFollowing: Boolean
)

@Serializable
data class GetFollowsResponse(
    val success: Boolean,
    val follows: List<FollowUserData> = listOf(),
    val message: String? = null
)