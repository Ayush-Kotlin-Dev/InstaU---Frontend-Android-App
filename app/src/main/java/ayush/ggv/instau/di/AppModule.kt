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
import ayush.ggv.instau.data.followunfollow.data.FollowRepositoryImpl
import ayush.ggv.instau.data.followunfollow.data.FollowService
import ayush.ggv.instau.data.followunfollow.domain.FollowRepository
import ayush.ggv.instau.data.postlike.data.PostLikeService
import ayush.ggv.instau.data.postlike.data.PostLikesRepositoryImpl
import ayush.ggv.instau.data.postlike.domain.repository.PostLikesRepository
import ayush.ggv.instau.data.posts.data.PostService
import ayush.ggv.instau.data.posts.data.PostsRepositoryImpl
import ayush.ggv.instau.data.posts.domain.repository.PostRepository
import ayush.ggv.instau.data.profile.data.ProfileRepositoryImpl
import ayush.ggv.instau.data.profile.data.ProfileService
import ayush.ggv.instau.data.profile.domain.repository.ProfileRepository
import ayush.ggv.instau.domain.usecases.followsusecase.FollowsUseCase
import ayush.ggv.instau.domain.usecases.followsusecase.GetFollowersUseCase
import ayush.ggv.instau.domain.usecases.followsusecase.GetFollowingUseCase
import ayush.ggv.instau.domain.usecases.postlikeusecase.PostLikeUseCase
import ayush.ggv.instau.domain.usecases.postsusecase.AddPostUseCase
import ayush.ggv.instau.domain.usecases.postsusecase.DeletePostUseCase
import ayush.ggv.instau.domain.usecases.postsusecase.GetPostByIdUseCase
import ayush.ggv.instau.domain.usecases.postsusecase.getPostsByuserIdUseCase
import ayush.ggv.instau.domain.usecases.postusecase.PostUseCase
import ayush.ggv.instau.domain.usecases.profileusecase.ProfileUseCase
import ayush.ggv.instau.domain.usecases.profileusecase.UpdateProfileUseCase
import ayush.ggv.instau.domain.usecases.signinusecase.SignInuseCase
import ayush.ggv.instau.domain.usecases.signupusecases.SignUpUseCase
import ayush.ggv.instau.presentation.components.PostListItemViewModel
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
    single  <ProfileRepository>{ProfileRepositoryImpl(get()) }
    single { FollowsUseCase() }
    single { GetFollowersUseCase() }
    single { GetFollowingUseCase() }
    single <FollowRepository>{ FollowRepositoryImpl(get()) }
    single <PostLikesRepository>{PostLikesRepositoryImpl(get())  }
    single { SignUpUseCase() }
    factory { AuthService() }
    factory { PostService() }
    factory { FollowService() }
    factory { PostLikeService() }
    factory { SignUpUseCase() }
    factory { SignInuseCase() }
    factory { PostUseCase() }
    factory {  AddPostUseCase()}
    factory { GetPostByIdUseCase() }
    factory {  ProfileService()}
    factory { ProfileUseCase() }
    factory { getPostsByuserIdUseCase() }
    factory { UpdateProfileUseCase() }
    factory { DeletePostUseCase() }
    factory { FollowsUseCase() }
    factory { PostLikeUseCase() }


    viewModel { SignUpViewModel(get() , get()) } //Provide DataStore<UserSettings> as an instance of DataStore<UserSettings>
    viewModel { LoginViewModel(get() , get()) } //Provide DataStore<UserSettings> as an instance of DataStore<UserSettings>
    viewModel { MainActivityViewModel(get()) }
    viewModel{ HomeScreenViewModel(get(),get () ) }
    viewModel { PostDetailScreenViewModel(get() ) }
    viewModel{ ProfileScreenViewModel( get() , get()  , get()) }
    viewModel { EditProfileViewModel(get() ,get()) }
    viewModel{ FollowsViewModel(get() , get()) }
    viewModel { AddPostViewModel( get()) }
    viewModel{PostListItemViewModel(get() , get() , get())  }

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