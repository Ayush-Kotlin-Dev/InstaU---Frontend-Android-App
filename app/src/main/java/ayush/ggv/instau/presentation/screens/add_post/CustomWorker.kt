import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.*
import ayush.ggv.instau.SocialApplication
import ayush.ggv.instau.domain.usecases.postsusecase.AddPostUseCase
import ayush.ggv.instau.model.PostParams
import ayush.ggv.instau.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.util.concurrent.TimeUnit

class UploadPostWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val addPostUseCase: AddPostUseCase by inject()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val imagePath = inputData.getString(KEY_IMAGE_PATH)
            val caption = inputData.getString(KEY_CAPTION)

            if (imagePath == null || caption == null) {
                return@withContext Result.failure(
                    workDataOf(KEY_ERROR to "Missing image path or caption")
                )
            }

            setForeground(createForegroundInfo("Uploading post..."))

            val imageFile = File(imagePath)
            val imageBytes = imageFile.readBytes()

            val result = addPostUseCase(
                imageUri = imageBytes,
                PostParams(userId = null, caption = caption)
            )

            imageFile.delete()

            return@withContext when (result) {
                is ayush.ggv.instau.util.Result.Success -> Result.success()
                is ayush.ggv.instau.util.Result.Error -> Result.failure(
                    workDataOf(KEY_ERROR to (result.message ?: "Unknown error"))
                )
                is ayush.ggv.instau.util.Result.Loading -> Result.retry()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error uploading post", e)
            Result.failure(workDataOf(KEY_ERROR to e.message))
        }
    }

    private fun createForegroundInfo(progress: String): ForegroundInfo {
        val notification = NotificationCompat.Builder(applicationContext, SocialApplication.UPLOAD_CHANNEL_ID)
            .setContentTitle("Uploading post")
            .setTicker(progress)
            .setSmallIcon(android.R.drawable.ic_menu_upload)
            .setOngoing(true)
            .setProgress(100, 0, true)
            .build()

        return ForegroundInfo(NOTIFICATION_ID, notification)
    }

    companion object {
        const val TAG = "UploadPostWorker"
        const val KEY_IMAGE_PATH = "imagePath"
        const val KEY_CAPTION = "caption"
        const val KEY_ERROR = "error"
        const val NOTIFICATION_ID = 1

        fun buildWorkRequest(imagePath: String, caption: String): OneTimeWorkRequest {
            val inputData = workDataOf(
                KEY_IMAGE_PATH to imagePath,
                KEY_CAPTION to caption
            )

            return OneTimeWorkRequestBuilder<UploadPostWorker>()
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.LINEAR, WorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                .build()
        }
    }
}