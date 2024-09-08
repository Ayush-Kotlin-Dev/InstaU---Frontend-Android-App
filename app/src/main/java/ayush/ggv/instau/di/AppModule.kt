package ayush.ggv.instau.di

import ChatService
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.room.Room
import ayush.ggv.instau.MainActivityViewModel
import ayush.ggv.instau.common.datastore.UserPreferences
import ayush.ggv.instau.common.datastore.UserPreferencesImpl
import ayush.ggv.instau.presentation.screens.auth.signup.SignUpViewModel
import ayush.ggv.instau.presentation.screens.auth.login.LoginViewModel
import ayush.ggv.instau.common.datastore.UserSettingsSerializer
import ayush.ggv.instau.dao.post.PostsDatabase
import ayush.ggv.instau.data.auth.data.AuthRepositoryImpl
import ayush.ggv.instau.data.auth.data.AuthService
import ayush.ggv.instau.data.KtorApi
import ayush.ggv.instau.data.auth.domain.repository.AuthRepository
import ayush.ggv.instau.data.chat.data.ChatRepositoryImpl
import ayush.ggv.instau.data.chat.domain.ChatRepository
import ayush.ggv.instau.data.followunfollow.data.FollowRepositoryImpl
import ayush.ggv.instau.data.followunfollow.data.FollowService
import ayush.ggv.instau.data.followunfollow.domain.FollowRepository
import ayush.ggv.instau.data.notification.NotificationService
import ayush.ggv.instau.data.onboarding.data.DataStoreOperationImpl
import ayush.ggv.instau.data.onboarding.data.OnboardingRepositoryImpl
import ayush.ggv.instau.data.onboarding.domain.DataStoreOperations
import ayush.ggv.instau.data.onboarding.domain.OnboardingRepository
import ayush.ggv.instau.data.postcomments.data.PostCommentService
import ayush.ggv.instau.data.postcomments.data.PostCommentsRepositoryImpl
import ayush.ggv.instau.data.postcomments.domain.PostCommentsRepository
import ayush.ggv.instau.data.postlike.data.PostLikeService
import ayush.ggv.instau.data.postlike.data.PostLikesRepositoryImpl
import ayush.ggv.instau.data.postlike.domain.repository.PostLikesRepository
import ayush.ggv.instau.data.posts.data.PostService
import ayush.ggv.instau.data.posts.data.PostsRepositoryImpl
import ayush.ggv.instau.data.posts.domain.repository.PostRepository
import ayush.ggv.instau.data.profile.data.ProfileRepositoryImpl
import ayush.ggv.instau.data.profile.data.ProfileService
import ayush.ggv.instau.data.profile.domain.repository.ProfileRepository
import ayush.ggv.instau.data.qna.data.QnaRepositoryImpl
import ayush.ggv.instau.data.qna.data.QnaService
import ayush.ggv.instau.data.qna.domain.QnaRepository
import ayush.ggv.instau.domain.usecases.chat_service.FriendListUseCase
import ayush.ggv.instau.domain.usecases.chat_service.GetRoomHistoryUseCase
import ayush.ggv.instau.domain.usecases.followsusecase.FollowsUseCase
import ayush.ggv.instau.domain.usecases.followsusecase.GetFollowersUseCase
import ayush.ggv.instau.domain.usecases.followsusecase.GetFollowingUseCase
import ayush.ggv.instau.domain.usecases.followsusecase.SuggestionsUseCase
import ayush.ggv.instau.domain.usecases.postcommentusecase.CommentUseCase
import ayush.ggv.instau.domain.usecases.postcommentusecase.DeleteCommentUseCase
import ayush.ggv.instau.domain.usecases.postcommentusecase.GetCommentsUseCase
import ayush.ggv.instau.domain.usecases.postlikeusecase.PostLikeUseCase
import ayush.ggv.instau.domain.usecases.postsusecase.AddPostUseCase
import ayush.ggv.instau.domain.usecases.postsusecase.DeletePostUseCase
import ayush.ggv.instau.domain.usecases.postsusecase.GetPostByIdUseCase
import ayush.ggv.instau.domain.usecases.postsusecase.GetPostsStreamUseCase
import ayush.ggv.instau.domain.usecases.postsusecase.getPostsByuserIdUseCase
import ayush.ggv.instau.domain.usecases.profileusecase.ProfileUseCase
import ayush.ggv.instau.domain.usecases.profileusecase.SearchUserUseCase
import ayush.ggv.instau.domain.usecases.profileusecase.UpdateProfileUseCase
import ayush.ggv.instau.domain.usecases.qnausecase.AddAnswerUseCase
import ayush.ggv.instau.domain.usecases.qnausecase.AddQuestionUseCase
import ayush.ggv.instau.domain.usecases.qnausecase.QnaDetailUseCase
import ayush.ggv.instau.domain.usecases.qnausecase.QnaUseCase
import ayush.ggv.instau.domain.usecases.signinusecase.SignInuseCase
import ayush.ggv.instau.domain.usecases.signupusecases.SignUpUseCase
import ayush.ggv.instau.model.GetCommentsResponse
import ayush.ggv.instau.presentation.screens.account.edit.EditProfileViewModel
import ayush.ggv.instau.presentation.screens.account.follows.FollowsViewModel
import ayush.ggv.instau.presentation.screens.account.profile.ProfileScreenViewModel
import ayush.ggv.instau.presentation.screens.add_post.AddPostScreen
import ayush.ggv.instau.presentation.screens.add_post.AddPostViewModel
import ayush.ggv.instau.presentation.screens.chat.friends_list.FriendListScreenViewModel
import ayush.ggv.instau.presentation.screens.chat.single_chat.ChatRoomViewModel
import ayush.ggv.instau.presentation.screens.home.HomeScreenViewModel
import ayush.ggv.instau.presentation.screens.post.PostDetailScreenViewModel
import ayush.ggv.instau.presentation.screens.qna.QnaViewModel
import ayush.ggv.instau.presentation.screens.qna.qna_detailed.QnaDetailViewModel
import ayush.ggv.instau.presentation.screens.search.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<DataStoreOperations> { DataStoreOperationImpl(androidContext()) }
    single<OnboardingRepository>{ OnboardingRepositoryImpl(get()) }
    single<KtorApi> { AuthService() } // Provide AuthService as an instance of KtorApi
    single<AuthRepository> { AuthRepositoryImpl(get(),get(),get()) }
    single {
        Room.databaseBuilder(
            androidContext(),
            PostsDatabase::class.java,
            "posts_database"
        ).fallbackToDestructiveMigration()
            .build()
    }
    single<UserPreferences> { UserPreferencesImpl(get()) }
    single <PostRepository> { PostsRepositoryImpl(get() ,  get() , get()) }
    single  <ProfileRepository>{ProfileRepositoryImpl(get(),get()) }
    single  <PostCommentsRepository> {PostCommentsRepositoryImpl(get(), get() ) }
    single { FollowsUseCase() }
    single { GetFollowersUseCase() }
    single { GetFollowingUseCase() }
    single <FollowRepository>{ FollowRepositoryImpl(get(),get()) }
    single <PostLikesRepository>{PostLikesRepositoryImpl(get(),get())  }
    single <ChatRepository>{ChatRepositoryImpl(get(), get()) }
    single <QnaRepository>{ QnaRepositoryImpl(get(),get()) }
    factory { ChatService() }

    factory { AuthService() }
    factory { PostService() }
    factory { FollowService() }
    factory { PostLikeService() }
    factory { PostCommentService() }
    factory {NotificationService() }
    factory { QnaService() }

    factory { SignUpUseCase() }
    factory { SignInuseCase() }
    factory {  AddPostUseCase()}
    factory { GetPostByIdUseCase() }
    factory {  ProfileService()}
    factory { ProfileUseCase() }
    factory { getPostsByuserIdUseCase() }
    factory { UpdateProfileUseCase() }
    factory { DeletePostUseCase() }
    factory { GetPostsStreamUseCase() }
    factory { FollowsUseCase() }
    factory { PostLikeUseCase() }
    factory {CommentUseCase() }
    factory { GetCommentsUseCase() }
    factory { DeleteCommentUseCase() }
    factory { SuggestionsUseCase() }
    factory { SearchUserUseCase() }
    factory { FriendListUseCase() }
    factory { GetRoomHistoryUseCase() }
    factory { QnaUseCase() }
    factory { QnaDetailUseCase() }
    factory { AddAnswerUseCase() }
    factory { AddQuestionUseCase() }
    viewModel { SignUpViewModel(get() , get(), get()) } //Provide DataStore<UserSettings> as an instance of DataStore<UserSettings>
    viewModel { LoginViewModel(get() , get() , get()) } //Provide DataStore<UserSettings> as an instance of DataStore<UserSettings>
    viewModel { MainActivityViewModel(get()) }
    viewModel{ HomeScreenViewModel(get(),get () , get() ,get() , get() , get() , get()) }
    viewModel { PostDetailScreenViewModel(get(), get(), get(), get()  ) }
    viewModel{ ProfileScreenViewModel( get() , get()  , get() , get() ) }
    viewModel { EditProfileViewModel(get() ,get()) }
    viewModel{ FollowsViewModel(get() , get()) }
    viewModel { AddPostViewModel( get() , androidContext()) }
    viewModel{SearchViewModel(get())}
    viewModel{FriendListScreenViewModel(get() ,  get() )}
    viewModel{ChatRoomViewModel(get() , get() )}
    viewModel{QnaViewModel(get(),get())}
    viewModel{QnaDetailViewModel(get() , get())}

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