package ayush.ggv.instau.common.fakedata

data class Post(
    val id: String,
    val text: String,
    val imageUrl: String,
    val createdAt: String,
    val likesCount: Int,
    val commentCount: Int,
    val authorId: Int,
    val authorName: String,
    val authorImage: String,
    val isLiked: Boolean = false,
    val isOwnPost: Boolean = false
)


//Dummy Posts
//Dummy Posts
val samplePosts = listOf(
    Post(
        id = "11",
        text = "This is a beautiful day! Enjoying the sunshine and the great weather.",
        imageUrl = "https://picsum.photos/408",
        createdAt = "20 min",
        likesCount = 12,
        commentCount = 3,
        authorId = 1,
        authorName = "Ayush",
        authorImage = "https://picsum.photos/201"
    ),
    Post(
        id = "12",
        text = "Just finished a great book. Highly recommend 'The Great Gatsby' to anyone who loves classic literature.",
        imageUrl = "https://picsum.photos/401",
        createdAt = "May 03, 2023",
        likesCount = 121,
        commentCount = 23,
        authorId = 2,
        authorName = "Vaibhav",
        authorImage = "https://picsum.photos/202"
    ),
    Post(
        id = "13",
        text = "Spent the day hiking in the mountains. The view from the top was breathtaking!",
        imageUrl = "https://picsum.photos/400",
        createdAt = "Apr 12, 2023",
        likesCount = 221,
        commentCount = 41,
        authorId = 3,
        authorName = "Paras",
        authorImage = "https://picsum.photos/203"
    ),
    Post(
        id = "14",
        text = "Had a great time at the concert last night. The band was amazing!",
        imageUrl = "https://picsum.photos/399",
        createdAt = "Mar 31, 2023",
        likesCount = 90,
        commentCount = 13,
        authorId = 3,
        authorName = "Omkar",
        authorImage = "https://picsum.photos/204"
    ),
    Post(
        id = "15",
        text = "Just finished a great book. Highly recommend 'The Great Gatsby' to anyone who loves classic literature.",
        imageUrl = "https://picsum.photos/404",
        createdAt = "May 03, 2023",
        likesCount = 121,
        commentCount = 23,
        authorId = 2,
        authorName = "KhaoMasam",
        authorImage = "https://picsum.photos/205"
    ),
)