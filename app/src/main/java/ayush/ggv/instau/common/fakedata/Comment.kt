package ayush.ggv.instau.common.fakedata

data class Comment(
    val id: String,
    val comment: String,
    val date: String,
    val authorName: String,
    val authorImageUrl: String,
    val authorId: Int,
    val postId: Long
)

val sampleComments = listOf(
    Comment(
        id = "comment1",
        date = "2023-06-24",
        comment = "Great post!\nI learned a lot from it.",
        authorName = sampleUsers[0].name,
        authorImageUrl = sampleUsers[0].profileUrl,
        authorId = sampleUsers[0].id,
        postId = samplePosts[0].postId
    ),
    Comment(
        id = "comment2",
        date = "2023-06-24",
        comment = "Nice work! Keep sharing more content like this.",
        authorName = sampleUsers[1].name,
        authorImageUrl = sampleUsers[1].profileUrl,
        authorId = sampleUsers[1].id,
        postId = samplePosts[0].postId
    ),
    Comment(
        id = "comment3",
        date = "2023-06-24",
        comment = "Thanks for the insights!\nYour post was really helpful.",
        authorName = sampleUsers[2].name,
        authorImageUrl = sampleUsers[2].profileUrl,
        authorId = sampleUsers[2].id,
        postId = samplePosts[0].postId
    ),
    Comment(
        id = "comment4",
        date = "2023-06-24",
        comment = "I enjoyed reading your post! Looking forward to more.",
        authorName = sampleUsers[3].name,
        authorImageUrl = sampleUsers[3].profileUrl,
        authorId = sampleUsers[3].id,
        postId = samplePosts[0].postId
    )
)
