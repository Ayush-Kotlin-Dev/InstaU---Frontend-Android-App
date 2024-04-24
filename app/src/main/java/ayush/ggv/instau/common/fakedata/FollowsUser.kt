package ayush.ggv.instau.common.fakedata

import androidx.compose.runtime.Composable

data class FollowsUser(
    val id: Int,
    val name: String,
    val profileUrl: String,
    val isFollowing: Boolean = false
)

val sampleUsers = listOf(
    FollowsUser(
        id = 1,
        name = "Ayush",
        profileUrl = "https://picsum.photos/201"
    ),
    FollowsUser(
        id = 2,
        name = "Vaibhav",
        profileUrl = "https://picsum.photos/202"
    ),
    FollowsUser(
        id = 3,
        name = "Paras",
        profileUrl = "https://picsum.photos/203"
    ),
    FollowsUser(
        id = 4,
        name = "Omkar",
        profileUrl = "https://picsum.photos/204"
    )
)