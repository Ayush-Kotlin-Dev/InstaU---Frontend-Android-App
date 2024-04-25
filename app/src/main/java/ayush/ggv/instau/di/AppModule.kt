package ayush.ggv.instau.di

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import ayush.ggv.instau.MainActivityViewModel
import ayush.ggv.instau.presentation.auth.signup.SignUpViewModel
import ayush.ggv.instau.presentation.auth.login.LoginViewModel
import ayush.ggv.instau.common.datastore.UserSettingsSerializer
import ayush.ggv.instau.data.auth.data.AuthRepositoryImpl
import ayush.ggv.instau.data.auth.data.AuthService
import ayush.ggv.instau.data.auth.data.KtorApi
import ayush.ggv.instau.data.auth.domain.repository.AuthRepository
import ayush.ggv.instau.data.auth.domain.usecases.signinusecase.SignInuseCase
import ayush.ggv.instau.data.auth.domain.usecases.signupusecases.SignUpUseCase
import ayush.ggv.instau.presentation.home.HomeScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<KtorApi> { AuthService() } // Provide AuthService as an instance of KtorApi
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single { SignUpUseCase() }
    factory { AuthService() }
    factory { SignUpUseCase() }
    factory { SignInuseCase() }
    viewModel { SignUpViewModel(get() , get()) } //Provide DataStore<UserSettings> as an instance of DataStore<UserSettings>
    viewModel { LoginViewModel(get() , get()) } //Provide DataStore<UserSettings> as an instance of DataStore<UserSettings>
    viewModel { MainActivityViewModel(get()) }
    viewModel{ HomeScreenViewModel() }
    single{
        DataStoreFactory.create(
            serializer = UserSettingsSerializer,
            produceFile = {
                androidContext().dataStoreFile(
                    fileName = "app_user_settings",
                )
            }
        )
    }
}