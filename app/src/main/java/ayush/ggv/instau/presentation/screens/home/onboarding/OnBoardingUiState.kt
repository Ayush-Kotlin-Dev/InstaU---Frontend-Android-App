package ayush.ggv.instau.presentation.screens.home.onboarding

import instaU.ayush.com.model.FollowUserData


data class OnBoardingUiState(
    val isLoading: Boolean = false,
    val users : List<FollowUserData> = listOf(),
    val error: String? = null,
    val shouldShowOnBoarding : Boolean = false
)

