package ayush.ggv.instau.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ayush.ggv.instau.R
import ayush.ggv.instau.presentation.destinations.HomeDestination
import ayush.ggv.instau.presentation.destinations.LoginDestination
import ayush.ggv.instau.presentation.destinations.PostDetailDestination
import ayush.ggv.instau.presentation.destinations.SignUpNDestination
import ayush.ggv.instau.ui.theme.SmallElevation
import com.ramcosta.composedestinations.utils.currentDestinationAsState

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    val currentDestination = navHostController.currentDestinationAsState().value

    Surface(
        elevation = SmallElevation,
        modifier = modifier
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = getAppBarTitle(currentDestination?.route)),
                    style = MaterialTheme.typography.h6,
                )
            },
            modifier = modifier,
            backgroundColor = MaterialTheme.colors.surface,
            actions = {
                AnimatedVisibility(visible = currentDestination?.route == HomeDestination.route) {
                    IconButton(
                        onClick = {
                            navHostController.navigate(LoginDestination.route) {
                                popUpTo(HomeDestination.route) { inclusive = true }
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.person_circle_icon),
                            contentDescription = "Profile Screen "
                        )
                    }


                }
            },
            navigationIcon = {
                AnimatedVisibility(visible = shouldShowNavigationIcon(currentDestination?.route)) {
                    IconButton(
                        onClick = {
                            navHostController.navigateUp()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.round_arrow_back),
                            contentDescription = "Back"
                        )
                    }
                }
            }

        )
    }
}

//Helper fun ti get AppBar Title
fun getAppBarTitle(currentDestinationRoute: String?): Int {
    return when (currentDestinationRoute) {
        LoginDestination.route -> R.string.login_destination_title
        SignUpNDestination.route -> R.string.signup_destination_title
        HomeDestination.route -> R.string.home_destination_title
        PostDetailDestination.route -> R.string.post_detail_destination_title
        else -> R.string.app_name

    }
}

// should show navigation icon only when current destination is not HomeDestination
fun shouldShowNavigationIcon(currentDestinationRoute: String?): Boolean {
    return !(
            currentDestinationRoute == HomeDestination.route
                    || currentDestinationRoute == LoginDestination.route
                    || currentDestinationRoute == SignUpNDestination.route
            )
}