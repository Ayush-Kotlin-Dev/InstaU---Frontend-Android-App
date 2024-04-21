package ayush.ggv.instau.di

import ayush.ggv.instau.auth.signup.SignUpViewModel
import ayush.ggv.instau.auth.login.LoginViewModel
import ayush.ggv.instau.data.auth.data.AuthRepositoryImpl
import ayush.ggv.instau.data.auth.data.AuthService
import ayush.ggv.instau.data.auth.data.KtorApi
import ayush.ggv.instau.data.auth.domain.repository.AuthRepository
import ayush.ggv.instau.data.auth.domain.usecases.signinusecase.SignInuseCase
import ayush.ggv.instau.data.auth.domain.usecases.signupusecases.SignUpUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<KtorApi> { AuthService() } // Provide AuthService as an instance of KtorApi
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single { SignUpUseCase() }
    factory { AuthService() }
    factory { SignUpUseCase() }
    factory { SignInuseCase() }
    viewModel { SignUpViewModel(get()) }
    viewModel { LoginViewModel(get()) }
}