package ayush.ggv.instau.presentation.screens.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.paging.compose.collectAsLazyPagingItems
import ayush.ggv.instau.presentation.screens.destinations.Destination
import ayush.ggv.instau.presentation.screens.destinations.ProfileDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import ayush.ggv.instau.util.Result

@Composable
@com.ramcosta.composedestinations.annotation.Destination
fun Search(
    navigator: DestinationsNavigator,
    currentUserId:Long,
    token: String
) {
    val searchViewModel: SearchViewModel = koinViewModel()
    val searchQuery by searchViewModel.searchQuery
    val heroesState = searchViewModel.searchedHeroes
    SearchScreen(
        heroes = heroesState,
        searchQuery = searchQuery,
        onTextChange = {newValue ->
            searchViewModel.updateSearchQuery(newValue)
            searchViewModel.searchHeroes(searchQuery, token)
        },
        onSearchClicked = {
            searchViewModel.searchHeroes(searchQuery, token)
        },
        onCloseClicked = {
            navigator.popBackStack()
        },
        onItemClick = { userId ->
            navigator.navigate(ProfileDestination(userId, currentUserId, token))
        }

    )
}
