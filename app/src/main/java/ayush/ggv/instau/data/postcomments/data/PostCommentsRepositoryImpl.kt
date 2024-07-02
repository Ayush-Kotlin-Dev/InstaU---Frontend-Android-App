package ayush.ggv.instau.data.postcomments.data

import ayush.ggv.instau.common.datastore.UserPreferences
import ayush.ggv.instau.data.postcomments.domain.PostCommentsRepository
import ayush.ggv.instau.model.CommentResponse
import ayush.ggv.instau.model.GetCommentsResponse
import ayush.ggv.instau.model.NewCommentParams
import ayush.ggv.instau.model.RemoveCommentParams
import ayush.ggv.instau.util.Result
class PostCommentsRepositoryImpl(
    private val postCommentService: PostCommentService,
    private val userPreferences: UserPreferences

) : PostCommentsRepository {
    override suspend fun addComment(params: NewCommentParams, ): Result<CommentResponse> {

         return try{
            val userData  = userPreferences.getUserData()
             val updatedParams = params.copy(userId = userData.id) // Update params with userId
             val response = postCommentService.addComment(updatedParams , userData.token )
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

    override suspend fun removeComment(params: RemoveCommentParams): Result<CommentResponse> {
        return try{
            val userData = userPreferences.getUserData()
            val response = postCommentService.removeComment(params.copy(userId = userData.id) , userData.token )
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
        pageSize: Int
    ): Result<GetCommentsResponse> {
        return try{
            val token = userPreferences.getUserData().token
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