package ayush.ggv.instau.presentation.screens.search

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ayush.ggv.instau.presentation.components.ListContent
import ayush.ggv.instau.ui.theme.Purple700
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import instaU.ayush.com.model.FollowUserData

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
fun SearchScreen(
    heroes: UsersUiState,
    searchQuery: String,
    onTextChange: (String) -> Unit,
    onSearchClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    onItemClick :(Long)-> Unit
) {


    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = if(isSystemInDarkTheme()) Color.Black else Purple700
    )
    systemUiController.setNavigationBarColor(
        color = if(isSystemInDarkTheme()) Color.Black else MaterialTheme.colors.surface.copy(alpha = 0.95f)
    )


    Scaffold(
        topBar = {
            SearchTopBar(
                text = searchQuery,
                onTextChange = {
                    onTextChange(it)
                },
                onCloseClicked = {
                  onCloseClicked()
                },
                onSearchClicked = {
                    onSearchClicked()
                },
            )
        },
        content = {
            when {
                heroes.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                heroes.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Error: ${heroes.error}")
                    }
                }
                else -> {
                    ListContent(users = heroes ,)
                }
            }
        }
    )
}