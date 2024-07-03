package ayush.ggv.instau.presentation.screens.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.domain.usecases.profileusecase.SearchUserUseCase
import ayush.ggv.instau.model.Post
import instaU.ayush.com.model.GetFollowsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ayush.ggv.instau.util.Result
import instaU.ayush.com.model.FollowUserData

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val useCase: SearchUserUseCase
) : ViewModel() {

    private val _searchQuery = mutableStateOf("")

    val searchQuery = _searchQuery

    var searchedHeroes by mutableStateOf(UsersUiState())
        private set

    private var searchJob: Job? = null

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query.trim()

        // Cancel any previous search job
        searchJob?.cancel()

        // Start a new search job with debounce
        searchJob = viewModelScope.launch {
            delay(300)  // debounce time in milliseconds
            if (_searchQuery.value.isNotEmpty()) {
                searchHeroes(_searchQuery.value)
            } else {
                searchedHeroes = UsersUiState()  // Clear the results if the query is empty
            }
        }
    }

     fun searchHeroes(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (query.isEmpty()) {
                searchedHeroes = UsersUiState(
                    error = "Search query cannot be empty."
                )
                return@launch
            }

            searchedHeroes = searchedHeroes.copy(isLoading = true, error = null)
            val result = useCase(query)
            when (result) {
                is Result.Success -> {
                    val convertedUser = result.data?.follows?.map {
                        FollowUserData(
                            id = it.id,
                            name = it.name,
                            bio = it.bio,
                            imageUrl = it.imageUrl,
                            isFollowing = it.isFollowing
                        )
                    }
                    searchedHeroes = searchedHeroes.copy(
                        users = convertedUser ?: listOf(),
                        isLoading = false
                    )
                }

                is Result.Error -> {
                    val cleanedErrorMessage = result.message?.substringAfter("Error: ")
                    searchedHeroes = searchedHeroes.copy(
                        error = cleanedErrorMessage,
                        isLoading = false
                    )
                }

                is Result.Loading -> TODO()
            }
        }
    }
}

data class UsersUiState(
    val isLoading: Boolean = false,
    val users: List<FollowUserData> = listOf(),
    val error: String? = null
)