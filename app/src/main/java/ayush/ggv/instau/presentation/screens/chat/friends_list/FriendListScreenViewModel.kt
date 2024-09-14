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
import java.util.Locale

class FriendListScreenViewModel(
    private val dataStore: DataStore<UserSettings>,
    private val useCase: FriendListUseCase
) : ViewModel() {

    val currentUserId = mutableLongStateOf(-1L)
    val userAvatar = mutableStateOf("")

    private val _searchState = mutableStateOf("")
    val searchState: State<String> = _searchState

    private val _friendListState = mutableStateOf(FriendListState())
    val friendListState: State<FriendListState> = _friendListState

    private val _filteredFriendListState = mutableStateOf<List<FriendList.FriendInfo>>(emptyList())
    val filteredFriendListState: State<List<FriendList.FriendInfo>> = _filteredFriendListState

    fun onSearchTextChange(query: String) {
        _searchState.value = query
        filterFriendList(query)
    }

    private fun filterFriendList(query: String) {
        val lowercaseQuery = query.lowercase(Locale.ROOT)
        _filteredFriendListState.value = _friendListState.value.data.filter { friend ->
            friend.username.lowercase(Locale.ROOT).contains(lowercaseQuery)
        }
    }

    fun getFriendList() {
        viewModelScope.launch {
            dataStore.data.map { it.toAuthResultData() }.collect { userSettings ->
                currentUserId.longValue = userSettings.id
                userAvatar.value = userSettings.avatar.orEmpty()
                fetchFriendList()
            }
        }
    }

    private fun fetchFriendList() {
        viewModelScope.launch {
            _friendListState.value = FriendListState(isLoading = true)
            useCase(currentUserId.longValue).onEach {
                when (it) {
                    is ResponseResource.Error ->
                        _friendListState.value =
                            FriendListState(error = it.errorMessage.errorMessage.orEmpty())

                    is ResponseResource.Success -> {
                        val friendList = it.data.friendInfo.orEmpty()
                        _friendListState.value = FriendListState(data = friendList)
                        _filteredFriendListState.value = friendList // Initialize filtered list with all friends
                    }
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