package ayush.ggv.instau.data.onboarding.data

import ayush.ggv.instau.data.onboarding.domain.DataStoreOperations
import ayush.ggv.instau.data.onboarding.domain.OnboardingRepository
import kotlinx.coroutines.flow.Flow

class OnboardingRepositoryImpl(
    private val dataStoreOperations: DataStoreOperations
): OnboardingRepository {
    override suspend fun saveOnBoardingState(completed: Boolean) {
        // Save onboarding state
        dataStoreOperations.saveOnBoardingState(completed)

    }

    override fun getOnBoardingState(): Flow<Boolean> {
        // Get onboarding state
        return dataStoreOperations.getOnBoardingState()

    }
}