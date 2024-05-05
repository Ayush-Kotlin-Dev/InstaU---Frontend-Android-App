package ayush.ggv.instau.di

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import ayush.ggv.instau.MainActivityViewModel
import ayush.ggv.instau.presentation.screens.auth.signup.SignUpViewModel
import ayush.ggv.instau.presentation.screens.auth.login.LoginViewModel
import ayush.ggv.instau.common.datastore.UserSettingsSerializer
import ayush.ggv.instau.data.auth.data.AuthRepositoryImpl
import ayush.ggv.instau.data.auth.data.AuthService
import ayush.ggv.instau.data.KtorApi
import ayush.ggv.instau.data.auth.domain.repository.AuthRepository
import ayush.ggv.instau.data.posts.data.PostService
import ayush.ggv.instau.data.posts.data.PostsRepositoryImpl
import ayush.ggv.instau.data.posts.domain.repository.PostRepository
import ayush.ggv.instau.domain.usecases.postsusecase.AddPostUseCase
import ayush.ggv.instau.domain.usecases.postusecase.PostUseCase
import ayush.ggv.instau.domain.usecases.signinusecase.SignInuseCase
import ayush.ggv.instau.domain.usecases.signupusecases.SignUpUseCase
import ayush.ggv.instau.presentation.screens.account.edit.EditProfileViewModel
import ayush.ggv.instau.presentation.screens.account.follows.FollowsViewModel
import ayush.ggv.instau.presentation.screens.account.profile.ProfileScreenViewModel
import ayush.ggv.instau.presentation.screens.add_post.AddPostScreen
import ayush.ggv.instau.presentation.screens.add_post.AddPostViewModel
import ayush.ggv.instau.presentation.screens.home.HomeScreenViewModel
import ayush.ggv.instau.presentation.screens.post.PostDetailScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<KtorApi> { AuthService() } // Provide AuthService as an instance of KtorApi
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single <PostRepository> { PostsRepositoryImpl(get()) }
    single { SignUpUseCase() }
    factory { AuthService() }
    factory { PostService() }
    factory { SignUpUseCase() }
    factory { SignInuseCase() }
    factory { PostUseCase() }
    factory {  AddPostUseCase()}
    viewModel { SignUpViewModel(get() , get()) } //Provide DataStore<UserSettings> as an instance of DataStore<UserSettings>
    viewModel { LoginViewModel(get() , get()) } //Provide DataStore<UserSettings> as an instance of DataStore<UserSettings>
    viewModel { MainActivityViewModel(get()) }
    viewModel{ HomeScreenViewModel(get(),get ()) }
    viewModel { PostDetailScreenViewModel() }
    viewModel{ ProfileScreenViewModel() }
    viewModel { EditProfileViewModel() }
    viewModel{ FollowsViewModel() }
    viewModel { AddPostViewModel( get()) }
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