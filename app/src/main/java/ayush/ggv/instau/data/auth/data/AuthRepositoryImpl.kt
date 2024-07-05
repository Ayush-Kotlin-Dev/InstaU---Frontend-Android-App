package ayush.ggv.instau.data.auth.data

import android.util.Log
import ayush.ggv.instau.common.datastore.UserPreferences
import ayush.ggv.instau.common.datastore.UserSettings
import ayush.ggv.instau.data.auth.domain.model.AuthResultData
import ayush.ggv.instau.data.auth.domain.repository.AuthRepository
import ayush.ggv.instau.data.onboarding.domain.OnboardingRepository
import ayush.ggv.instau.data.toAuthResultData
import ayush.ggv.instau.model.SignInRequest
import ayush.ggv.instau.model.SignUpRequest
import ayush.ggv.instau.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val userPreferences: UserPreferences, //TODO directly update the datasource from here
    private val onboardingRepository: OnboardingRepository
) : AuthRepository {

    override suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): Result<AuthResultData> = withContext(Dispatchers.IO) {
        try {

            val request = SignUpRequest(name, email, password)
            val authResponse = authService.signUp(request)
            if (authResponse.data == null) {
                Result.Error(
                    message =authResponse.errorMessage ?: "Sign up failed"
                )
            } else {
                val authResultData = authResponse.data.toAuthResultData()
                Log.d("HomeScreenViewModel", "Onboarding state Followers count : ${authResultData.followingCount}")

                if(authResultData.followingCount!=0 || authResultData.followersCount!=0){
                    Log.d("HomeScreenViewModel", "Onboarding state: ${onboardingRepository.getOnBoardingState()}")
                    onboardingRepository.saveOnBoardingState(true)
                    Log.d("HomeScreenViewModel", "Onboarding state after true: ${onboardingRepository.getOnBoardingState()}")

                }
                Result.Success(authResultData)
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Oops! we could not send your request. Please try again later.")
        }

    }

override suspend fun signIn(
        email: String,
        password: String
    ): Result<AuthResultData> = withContext(Dispatchers.IO) {
        try {
            val request = SignInRequest(email, password)
            val authResponse = authService.signIn(request)
            if (authResponse.data == null) {
                Result.Error(
                    message = authResponse.errorMessage ?: "Sign in failed"
                )
            } else {
                val authResultData = authResponse.data.toAuthResultData()
                Log.d("HomeScreenViewModel", "Onboarding state Followers count : ${authResultData.followingCount}")

                if(authResultData.followingCount!=0 || authResultData.followersCount!=0){
                    Log.d("HomeScreenViewModel", "Onboarding state: ${onboardingRepository.getOnBoardingState()}")
                    onboardingRepository.saveOnBoardingState(true)
                    Log.d("HomeScreenViewModel", "Onboarding state after true: ${onboardingRepository.getOnBoardingState()}")

                }
                Result.Success(authResultData)
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Oops! we could not send your request. Please try again later.")
        }
    }

}