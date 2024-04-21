package ayush.ggv.instau.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination


@Composable
@Destination
fun Home() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Home")
    }

}