package ayush.ggv.instau.presentation.screens.chat.friends_list

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.common.datastore.UserSettings
import ayush.ggv.instau.common.datastore.toAuthResultData
import ayush.ggv.instau.domain.usecases.chat_service.FriendListUseCase
import ayush.ggv.instau.model.FriendList
import ayush.ggv.instau.util.ResponseResource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FriendListScreenViewModel(
    private val dataStore: DataStore<UserSettings>,
    private val useCase: FriendListUseCase
) : ViewModel() {

    val token = mutableStateOf("")
    val currentUserId = mutableLongStateOf(-1L)
    val userAvatar = mutableStateOf("")

    private val _searchState = mutableStateOf("")
    val searchState: State<String> = _searchState
    fun onSearchTextChange(result: String) {
        // TODO: I'm too lazy to handle search :D
        _searchState.value = result
    }

    private val _friendListState = mutableStateOf(FriendListState())
    val friendListState: State<FriendListState> = _friendListState

    fun getFriendList() {
        viewModelScope.launch {
            // Fetch token and currentUserId
            dataStore.data.map { it.toAuthResultData() }.collect { userSettings ->
                currentUserId.value = userSettings.id
                token.value = userSettings.token
                userAvatar.value = userSettings.avatar.orEmpty()
                // Once token and currentUserId are fetched, call useCase
                fetchFriendList()
            }
        }
    }

    private fun fetchFriendList() {
        viewModelScope.launch {
            _friendListState.value = FriendListState(isLoading = true)
            useCase(currentUserId.value, token.value).onEach {
                when (it) {
                    is ResponseResource.Error ->
                        _friendListState.value =
                            FriendListState(error = it.errorMessage.errorMessage.orEmpty())

                    is ResponseResource.Success ->
                        _friendListState.value =
                            FriendListState(data = it.data.friendInfo.orEmpty())
                }
                Log.d("FriendListScreenViewModel", "getFriendList: $it")
            }.launchIn(viewModelScope)
        }
    }
}

data class FriendListState(
    val isLoading: Boolean = false,
    val data: List<FriendList.FriendInfo> = emptyList(),
    val error: String = ""
)
