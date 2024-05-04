package ayush.ggv.instau.common.fakedata

data class Profile(
    val id: Int,
    val name: String,
    val bio: String,
    val profileUrl: String,
    val followersCount: Int,
    val followingCount: Int,
    val isOwnProfile: Boolean = false,
    val isFollowing: Boolean = false
)


val sampleProfiles = listOf(
    Profile(
        id = 1,
        name = "Ayush",
        bio = "Bio for Ayush",
        profileUrl = "https://picsum.photos/201",
        followersCount = 100,
        followingCount = 50
    ),

    Profile(
        id = 2,
        name = "Vaibhav",
        bio = "Bio for Vaibhav",
        profileUrl = "https://picsum.photos/202",
        followersCount = 200,
        followingCount = 75
    ),
    Profile(
        id = 3,
        name = "Paras",
        bio = "Bio for Paras",
        profileUrl = "https://picsum.photos/203",
        followersCount = 150,
        followingCount = 65
    )
)