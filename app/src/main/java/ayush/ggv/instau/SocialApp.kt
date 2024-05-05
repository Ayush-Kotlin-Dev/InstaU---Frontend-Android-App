package ayush.ggv.instau

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ayush.ggv.instau.presentation.components.AppBar
import ayush.ggv.instau.presentation.screens.NavGraphs
import ayush.ggv.instau.presentation.screens.destinations.HomeDestination
import ayush.ggv.instau.presentation.screens.destinations.LoginDestination
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable
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

    val currentDestination by navHostController.currentDestinationAsState()
    val navigationBarItems = remember { NavigationBarItems.values()}
    var selectedIndex by remember {
        mutableStateOf(0)
    }

    @SuppressLint("ModifierFactoryUnreferencedReceiver")
    fun Modifier.noRippleClickable(onClick :() -> Unit ): Modifier = composed {
        clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = onClick
        )
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
        },
        bottomBar =  {
            if(currentDestination?.route == HomeDestination.route) {
                AnimatedNavigationBar(
                    modifier = Modifier.height(64.dp),
                    selectedIndex = selectedIndex,
                    cornerRadius = shapeCornerRadius(34.dp),
                    ballAnimation = Parabolic(tween(300)),
                    indentAnimation = Height(tween(300)),
                    ballColor = MaterialTheme.colors.primary,
                    barColor = MaterialTheme.colors.surface,

                    ) {
                    navigationBarItems.forEach { item ->
                        Box(
                            modifier = Modifier
                                .noRippleClickable { selectedIndex = item.ordinal }
                                .fillMaxSize(),

                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = item.icons,
                                contentDescription = null,
                                tint = if (selectedIndex == item.ordinal) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(
                                    alpha = 0.6f
                                )
                            )

                        }
                    }

                }
            }

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
enum class NavigationBarItems (val icons : ImageVector){
    HOME(icons = Icons.Filled.Home),
    SEARCH(icons = Icons.Filled.Search),
    PROFILE(icons = Icons.Filled.Person)
}

