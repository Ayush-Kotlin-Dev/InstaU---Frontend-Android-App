package ayush.ggv.instau.data.postcomments.data

import ayush.ggv.instau.data.KtorApi
import ayush.ggv.instau.model.CommentResponse
import ayush.ggv.instau.model.GetCommentsResponse
import ayush.ggv.instau.model.NewCommentParams
import ayush.ggv.instau.model.RemoveCommentParams
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class PostCommentService : KtorApi() {
    suspend fun addComment(
        newCommentParams: NewCommentParams,
        token: String
    ): CommentResponse = client.post {
        endPoint(path = "/post/comments/create")

        headers {
            append("Authorization", "Bearer $token")
        }
        setBody(newCommentParams)
    }.body()

    suspend fun removeComment(
        commentParams: RemoveCommentParams ,
        token: String
    ): CommentResponse = client.post {
        endPoint(path = "/post/comments/delete")

        headers {
            append("Authorization", "Bearer $token")
        }
        setBody(commentParams)
    }.body()

    suspend fun getComments(
        postId: Long,
        page : Int,
        limit : Int,
        token: String
    ): GetCommentsResponse = client.post {
        endPoint(path = "/post/comments/$postId")

        headers {
            append("Authorization", "Bearer $token")
        }
        parameter("postId", postId)
        parameter("page", page)
        parameter("limit", limit)


    }.body()
}

