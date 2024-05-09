package ayush.ggv.instau.common.fakedata

import ayush.ggv.instau.model.Post


//Dummy Posts
val samplePosts = listOf(
    Post(
        postId = 11L,
        caption = "This is a beautiful day! Enjoying the sunshine and the great weather.",
        imageUrl = "https://picsum.photos/408",
        createdAt = "20 min",
        likesCount = 12,
        commentsCount = 3,
        userId = 1L,
        userName = "Ayush",
        userImageUrl = "https://picsum.photos/201",
        isLiked = false,
        isOwnPost = true
    ),
    Post(
        postId = 12L,
        caption = "Just finished a great book. Highly recommend 'The Great Gatsby' to anyone who loves classic literature.",
        imageUrl = "https://picsum.photos/401",
        createdAt = "May 03, 2023",
        likesCount = 121,
        commentsCount = 23,
        userId = 2L,
        userName = "Vaibhav",
        userImageUrl = "https://picsum.photos/202",
        isLiked = false,
        isOwnPost = true
    ),
    Post(
        postId = 13L,
        caption = "Spent the day hiking in the mountains. The view from the top was breathtaking!",
        imageUrl = "https://picsum.photos/400",
        createdAt = "Apr 12, 2023",
        likesCount = 221,
        commentsCount = 41,
        userId = 3L,
        userName = "Paras",
        userImageUrl = "https://picsum.photos/203",
        isLiked = false,
        isOwnPost = true
    ),
    Post(
        postId = 14L,
        caption = "Had a great time at the concert last night. The band was amazing!",
        imageUrl = "https://picsum.photos/399",
        createdAt = "Mar 31, 2023",
        likesCount = 90,
        commentsCount = 13,
        userId = 4L,
        userName = "Omkar",
        userImageUrl = "https://picsum.photos/204",
        isLiked = false,
        isOwnPost = true
    ),
)