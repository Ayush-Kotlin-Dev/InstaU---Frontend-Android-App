package ayush.ggv.instau.presentation.screens.add_post

import UploadPostWorker
import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import ayush.ggv.instau.domain.usecases.postsusecase.AddPostUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class YourWorkerFactory : WorkerFactory(), KoinComponent {
    private val addPostUseCase: AddPostUseCase by inject()

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            UploadPostWorker::class.java.name ->
                UploadPostWorker(appContext, workerParameters)
            else ->
                null
        }
    }
}