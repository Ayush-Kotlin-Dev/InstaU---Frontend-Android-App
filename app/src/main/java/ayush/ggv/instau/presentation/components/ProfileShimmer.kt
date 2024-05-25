package ayush.ggv.instau.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun ShimmerProfileScreenPlaceholder() {
    val isDarkTheme = isSystemInDarkTheme()
    val colors = if (isDarkTheme) {
        listOf(Color.DarkGray.copy(0.9f), Color.DarkGray.copy(0.2f), Color.DarkGray.copy(0.9f))
    } else {
        listOf(Color.LightGray.copy(0.9f), Color.LightGray.copy(0.2f), Color.LightGray.copy(0.9f))
    }
    val placeholderColor = if (isDarkTheme) Color.DarkGray else Color.LightGray

    ShimmerAnimation(colors = colors) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Placeholder for the profile image
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(placeholderColor)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Placeholder for the name
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .padding(16.dp)
                    .height(20.dp)
                    .background(placeholderColor)
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Placeholder for the bio
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(26.dp)
                    .background(placeholderColor)

            )

            Spacer(modifier = Modifier.height(4.dp))

            // Placeholder for the followers and following counts
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp , vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Placeholder for the followers count
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(20.dp)
                        .background(placeholderColor)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Placeholder for the following count
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(20.dp)
                        .background(placeholderColor)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            // Placeholder for the posts
            ShimmerPostListItemPlaceholder()

        }

    }
}