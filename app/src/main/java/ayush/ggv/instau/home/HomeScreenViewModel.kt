package ayush.ggv.instau.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.common.fakedata.Post
import ayush.ggv.instau.common.fakedata.samplePosts
import ayush.ggv.instau.common.fakedata.sampleUsers
import ayush.ggv.instau.home.onboarding.OnBoardingUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel(){

    var postsUiState by mutableStateOf(PostsUiState())
         private set

    var onBoardingUiState by mutableStateOf(OnBoardingUiState())
        private set

    init {
        fetchData()
    }

    //fetchPosts()
    fun fetchData(){
        onBoardingUiState = onBoardingUiState.copy(isLoading = true)
        postsUiState = postsUiState.copy(isLoading = true)

        viewModelScope.launch {
            delay(1000)

            onBoardingUiState = onBoardingUiState.copy(
                users = sampleUsers,
                isLoading = false,
                shouldShowOnBoarding = true
            )
            postsUiState = postsUiState.copy(
                posts = samplePosts,
                isLoading = false
            )
        }

    }
}



data class PostsUiState(
    val isLoading : Boolean = false,
    val posts : List<Post> = listOf(),
    val error : String? = null
)