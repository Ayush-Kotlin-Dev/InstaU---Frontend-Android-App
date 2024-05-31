package ayush.ggv.instau.presentation.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.twotone.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import ayush.ggv.instau.NavigationBarItems
import ayush.ggv.instau.R
import ayush.ggv.instau.presentation.screens.account.profile.ProfileScreenViewModel
import ayush.ggv.instau.presentation.screens.destinations.AddPostDestination
import ayush.ggv.instau.presentation.screens.destinations.ContactsDestination
import ayush.ggv.instau.presentation.screens.destinations.EditProfileDestination
import ayush.ggv.instau.presentation.screens.destinations.FollowersDestination
import ayush.ggv.instau.presentation.screens.destinations.FollowingDestination
import ayush.ggv.instau.presentation.screens.destinations.HomeDestination
import ayush.ggv.instau.presentation.screens.destinations.LoginDestination
import ayush.ggv.instau.presentation.screens.destinations.PostDetailDestination
import ayush.ggv.instau.presentation.screens.destinations.ProfileDestination
import ayush.ggv.instau.presentation.screens.destinations.SearchDestination
import ayush.ggv.instau.presentation.screens.destinations.SignUpNDestination
import ayush.ggv.instau.presentation.screens.destinations.WebSocketChatScreenDestination
import ayush.ggv.instau.ui.theme.SmallElevation
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    val currentDestination = navHostController.currentDestinationAsState().value
    val context = LocalContext.current
    val viewModel: ProfileScreenViewModel = koinViewModel()
    var showDialog by remember { mutableStateOf(false) }

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
                AnimatedVisibility(visible = currentDestination?.route == HomeDestination.route || currentDestination?.route == ProfileDestination.route) {
                    if(currentDestination?.route == HomeDestination.route){
                        //chat icon
                        IconButton(
                            onClick = {
                                navHostController.navigate(ContactsDestination.route)
                                Toast.makeText(context, "Chat Screen ", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(imageVector = Icons.TwoTone.Email, contentDescription = "ChatScreen")
                        }
                    }else{
                        IconButton(
                            onClick = {
                                showDialog = true
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.person_circle_icon),
                                contentDescription = "Profile Screen "
                            )
                        }
                    }



                }
            },
            navigationIcon = {
                AnimatedVisibility(visible = shouldShowNavigationIcon(currentDestination?.route)) {
                    IconButton(
                        onClick = {
                            navHostController.navigateUp()
                            //change the index of the bottom nav bar

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
    // Add the confirmation dialog
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            // Use a Surface for rounded corners
            Surface(shape = MaterialTheme.shapes.medium, elevation = 8.dp) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(text = "Logout", style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Are you sure you want to logout?")
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.End) {
                        TextButton(
                            onClick = { showDialog = false },
                        ) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                viewModel.logout()
                                navHostController.navigate(LoginDestination.route) {
                                    popUpTo(HomeDestination.route) { inclusive = true }
                                }
                                showDialog = false
                            }
                        ) {
                            Text("Confirm")//white color
                        }
                    }
                }
            }
        }
    }
}

//Helper fun ti get AppBar Title
fun getAppBarTitle(currentDestinationRoute: String?): Int {
    return when (currentDestinationRoute) {
        LoginDestination.route -> R.string.login_destination_title
        SignUpNDestination.route -> R.string.signup_destination_title
        HomeDestination.route -> R.string.home_destination_title
        PostDetailDestination.route -> R.string.post_detail_destination_title
        ProfileDestination.route -> R.string.profile_destination_title
        EditProfileDestination.route -> R.string.edit_profile_destination_title
        FollowingDestination.route -> R.string.following_text
        FollowersDestination.route -> R.string.followers_text

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

fun getNavigationBarIndex(route: String?): Int {
    return when (route) {
        HomeDestination.route -> NavigationBarItems.HOME.ordinal
        AddPostDestination.route -> NavigationBarItems.ADD.ordinal
        ProfileDestination.route -> NavigationBarItems.PROFILE.ordinal
        SearchDestination.route-> NavigationBarItems.SEARCH.ordinal
        else -> NavigationBarItems.HOME.ordinal // default to HOME
    }
}