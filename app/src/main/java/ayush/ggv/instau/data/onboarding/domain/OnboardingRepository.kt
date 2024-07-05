package ayush.ggv.instau.data.onboarding.domain

import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    suspend fun saveOnBoardingState(completed: Boolean)
    fun getOnBoardingState(): Flow<Boolean>

}