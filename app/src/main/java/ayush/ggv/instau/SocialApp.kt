package ayush.ggv.instau

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import ayush.ggv.instau.auth.NavGraphs
import com.ramcosta.composedestinations.DestinationsNavHost

@Composable
fun SocialApp() {
    val navHostController = rememberNavController()
    
    DestinationsNavHost(navGraph = NavGraphs.root , navController = navHostController)
}