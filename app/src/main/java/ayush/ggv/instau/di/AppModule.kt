package ayush.ggv.instau.di

import ayush.ggv.instau.auth.SignUpViewModel
import ayush.ggv.instau.auth.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { SignUpViewModel() }
    viewModel { LoginViewModel() }
}