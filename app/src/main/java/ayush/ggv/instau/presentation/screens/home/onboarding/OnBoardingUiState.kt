package ayush.ggv.instau.presentation.screens.home.onboarding

import ayush.ggv.instau.common.fakedata.FollowsUser

data class OnBoardingUiState(
    val isLoading: Boolean = false,
    val users : List<FollowsUser> = listOf(),
    val error: String? = null,
    val shouldShowOnBoarding : Boolean = false
)

