package ayush.ggv.instau

import android.app.Application
import ayush.ggv.instau.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SocialApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Add your modules here
            androidContext(this@SocialApplication)
            modules(appModule)

        }
    }
}