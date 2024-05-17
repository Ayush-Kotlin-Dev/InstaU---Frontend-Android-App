package ayush.ggv.instau.data.postcomments.data

import ayush.ggv.instau.data.postcomments.domain.PostCommentsRepository
import ayush.ggv.instau.model.CommentResponse
import ayush.ggv.instau.model.GetCommentsResponse
import ayush.ggv.instau.model.NewCommentParams
import ayush.ggv.instau.model.RemoveCommentParams
import ayush.ggv.instau.util.Result
class PostCommentsRepositoryImpl(
    private val postCommentService: PostCommentService

) : PostCommentsRepository {
    override suspend fun addComment(
        params: NewCommentParams,
        token : String
    ): Result<CommentResponse> {

         return try{
            val response = postCommentService.addComment(params , token )
            if(response.success){
                Result.Success(response)
            }else{
                Result.Error(Exception("Error: ${response.message}").toString())
            }
         }catch (
            e: Exception
         ){
            Result.Error(e.toString())
         }
    }

    override suspend fun removeComment(params: RemoveCommentParams , token: String): Result<CommentResponse> {
        return try{
            val response = postCommentService.removeComment(params , token )
            if(response.success){
                Result.Success(response)
            }else{
                Result.Error(Exception("Error: ${response.message}").toString())
            }
        }catch (
            e: Exception
        ){
            Result.Error(e.toString())
        }
    }

    override suspend fun getComments(
        postId: Long,
        pageNumber: Int,
        pageSize: Int,
        token: String
    ): Result<GetCommentsResponse> {
        return try{
            val response = postCommentService.getComments(postId , pageNumber , pageSize , token )
            if(response.success){
                Result.Success(response)
            }else{
                Result.Error(Exception("Error: ${response.message}").toString())
            }
        }catch (
            e: Exception
        ){
            Result.Error(e.toString())
        }
    }
}