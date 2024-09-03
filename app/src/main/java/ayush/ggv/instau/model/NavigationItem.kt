package ayush.ggv.instau.model

import ayush.ggv.instau.R


enum class NavigationItem(
    val title: String,
    val icon: Int
) {
    Home(
        icon = R.drawable.liked_heart_drawable,
        title = "Home"
    ),
    Qna(
        icon = R.drawable.baseline_whatshot_24,
        title = "QNA"
    ),
    Events(
        icon = R.drawable.person_circle_icon,
        title = "Events"
    ),
    Settings(
        icon = R.drawable.like_icon_outlined,
        title = "Settings"
    )
}