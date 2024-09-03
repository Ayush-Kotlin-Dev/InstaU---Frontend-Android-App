package ayush.ggv.instau.presentation.screens.add_post

import android.app.Notification
import androidx.work.Worker

import android.content.Context
import android.util.Log
import androidx.work.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class UploadPostWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val imagePath = inputData.getString("imagePath")
            val caption = inputData.getString("caption")

            if (imagePath == null || caption == null) {
                return@withContext Result.failure(
                    workDataOf("error" to "Missing image path or caption")
                )
            }

            // Simulate network delay and show foreground notification
            setForeground(createForegroundInfo("Uploading post..."))

            val imageFile = File(imagePath)
            val imageBytes = imageFile.readBytes()

            // TODO: Replace this with your actual upload logic
            val result = addPostUseCase(
                imageUri = imageBytes,
                PostParams(userId = null, caption = caption)
            )

            // Delete the temporary file
            imageFile.delete()

            return@withContext when (result) {
                is Result.Success -> Result.success()
                is Result.Error -> Result.failure(
                    workDataOf("error" to (result.message ?: "Unknown error"))
                )
                is Result.Loading -> Result.retry()
            }
        } catch (e: Exception) {
            Log.e("UploadPostWorker", "Error uploading post", e)
            Result.failure(workDataOf("error" to e.message))
        }
    }

    fun createForegroundInfo(progress: String): ForegroundInfo {
        val notification = Notification.Builder(applicationContext, "upload_post")
            .setContentTitle("Uploading post")
            .setProgress(100, 0, true)
            .build()

        return ForegroundInfo(1, notification)
    }

    companion object {
        const val TAG = "UploadPostWorker"
    }

}