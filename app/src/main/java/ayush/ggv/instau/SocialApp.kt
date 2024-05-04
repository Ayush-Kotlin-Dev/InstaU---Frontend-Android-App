package ayush.ggv.instau

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ayush.ggv.instau.presentation.components.AppBar
import ayush.ggv.instau.presentation.screens.NavGraphs
import ayush.ggv.instau.presentation.screens.destinations.HomeDestination
import ayush.ggv.instau.presentation.screens.destinations.LoginDestination
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.utils.currentDestinationAsState

@Composable
fun SocialApp(
    token: String? = null
) {


    val navHostController = rememberNavController()

    val scaffoldState = rememberScaffoldState()
    val systemUiController = rememberSystemUiController()

    val isSystemInDark = isSystemInDarkTheme()
    val statusBarColor = if (isSystemInDark) {
        MaterialTheme.colors.surface
    } else {
        MaterialTheme.colors.surface.copy(alpha = 0.95f)
    }
    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = !isSystemInDark
        )
    }
    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
//            if(currentDestination?.route != LoginDestination.route && currentDestination?.route != SignUpNDestination.route) {
//                AppBar(
//                    navHostController = navHostController
//                )
//            }
            AppBar(
                navHostController = navHostController
            )
        }

    ) { innerPadding ->
        DestinationsNavHost(
            modifier = Modifier.padding(innerPadding),
            navGraph = NavGraphs.root,
            navController = navHostController
        )

    }
    LaunchedEffect(key1 = token) {
        if (token != null && token.isEmpty()) {
            navHostController.navigate(LoginDestination.route) {
                popUpTo(HomeDestination.route) {
                    inclusive = true
                }
            }
        }


    }
}
