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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.twotone.Search
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
import androidx.navigation.compose.rememberNavController
import ayush.ggv.instau.presentation.components.AppBar
import ayush.ggv.instau.presentation.components.getNavigationBarIndex
import ayush.ggv.instau.presentation.screens.NavGraphs
import ayush.ggv.instau.presentation.screens.destinations.AddPostDestination
import ayush.ggv.instau.presentation.screens.destinations.HomeDestination
import ayush.ggv.instau.presentation.screens.destinations.LoginDestination
import ayush.ggv.instau.presentation.screens.destinations.ProfileDestination
import ayush.ggv.instau.presentation.screens.destinations.SearchDestination
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.utils.currentDestinationAsState

@Composable
fun SocialApp(
    token: String? = null,
    userId: Long? = null,
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

    val navigationBarItems = remember { NavigationBarItems.entries.toTypedArray() }
    var selectedIndex by remember {
        mutableStateOf(0)
    }
    val currentDestination by navHostController.currentDestinationAsState()

    if (currentDestination?.route != null && currentDestination?.route != ProfileDestination.route) {
        selectedIndex = getNavigationBarIndex(currentDestination?.route)
    }

    @SuppressLint("ModifierFactoryUnreferencedReceiver")
    fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
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
            if (currentDestination?.route != SearchDestination.route) {
                AppBar(navHostController = navHostController )
            }
        },
        bottomBar = {
            if (currentDestination?.route in listOf(
                    HomeDestination.route,
                    AddPostDestination.route,
                    ProfileDestination.route,
                    SearchDestination.route
                )) {
                Box(modifier = Modifier.padding(top = 10.dp)) {
                    AnimatedNavigationBar(
                        modifier = Modifier.height(64.dp),
                        selectedIndex = selectedIndex,
                        cornerRadius = shapeCornerRadius(34.dp),
                        ballAnimation = Parabolic(tween(300)),
                        indentAnimation = Height(tween(300)),
                        ballColor = MaterialTheme.colors.primary,
                        barColor = MaterialTheme.colors.surface,
                    ) {
                        navigationBarItems.forEachIndexed { index, item ->
                            Box(
                                modifier = Modifier
                                    .noRippleClickable {
                                        selectedIndex = item.ordinal
                                        when (item) {
                                            NavigationBarItems.HOME -> navHostController.navigate(HomeDestination.route)
                                            NavigationBarItems.SEARCH -> navHostController.navigate(SearchDestination(userId!!, token!!).route)
                                            NavigationBarItems.ADD -> navHostController.navigate(AddPostDestination(userId = userId!!, token).route)
                                            NavigationBarItems.PROFILE -> navHostController.navigate(ProfileDestination(userId!!, userId, token!!).route)
                                        }
                                    }
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = item.icons,
                                    contentDescription = null,
                                    tint = if (selectedIndex == index) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(
                                        alpha = 0.6f
                                    )
                                )
                            }
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

enum class NavigationBarItems(val icons: ImageVector) {
    HOME(Icons.Filled.Home),
    SEARCH(Icons.TwoTone.Search),
    ADD(Icons.Filled.Add),
    PROFILE(Icons.Filled.Person)
}
