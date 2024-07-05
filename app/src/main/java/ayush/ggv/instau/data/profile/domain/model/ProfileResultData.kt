package ayush.ggv.instau.data.profile.domain.model

import ayush.ggv.instau.common.datastore.UserSettings
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserParams(
    val userId: Long,
    val name: String,
    val bio: String,
    val imageUrl: String? = null
)

@Serializable
data class Profile(
    val id: Long,
    val name: String,
    val bio: String,
    val imageUrl: String? = null,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val isFollowing: Boolean,
    val isOwnProfile: Boolean
)

@Serializable
data class ProfileResponse(
    val success: Boolean,
    val profile: Profile? = null,
    val message: String? = null
)

fun UserSettings.toDomainProfile():Profile{
    return Profile(
        id = this.id,
        name = this.name,
        bio = this.bio,
        imageUrl = this.avatar,
        followersCount = this.followersCount,
        followingCount = this.followingCount,
        isFollowing = false,
        isOwnProfile = true
    )
}

fun Profile.toUserSettings():UserSettings{
    return UserSettings(
        id = this.id,
        name = this.name,
        bio = this.bio,
        avatar = this.imageUrl,
        followersCount = this.followersCount,
        followingCount = this.followingCount,
    )
}