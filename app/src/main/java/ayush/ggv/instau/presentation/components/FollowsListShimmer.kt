package ayush.ggv.instau.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ayush.ggv.instau.ui.theme.LargeSpacing
import ayush.ggv.instau.ui.theme.MediumSpacing

@Composable
fun ShimmerAnimation(
    content: @Composable () -> Unit
) {
    val colors = listOf(Color.LightGray.copy(alpha = 0.9f), Color.LightGray.copy(alpha = 0.2f), Color.LightGray.copy(alpha = 0.9f))
    val transition = rememberInfiniteTransition()
    val animatedProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1200, easing = LinearEasing),
            RepeatMode.Restart
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        content()
        Box(
            Modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = colors,
                            start = Offset(10f, 10f),
                            end = Offset(size.width, size.height)
                        ),
                        alpha = animatedProgress
                    )
                }
        )
    }
}
@Preview
@Composable
fun ShimmerFollowsListItemPlaceholder() {
    val colors = listOf(Color.LightGray.copy(0.9f), Color.LightGray.copy(0.2f), Color.LightGray.copy(0.9f))

    ShimmerAnimation(colors = colors) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LargeSpacing, vertical = MediumSpacing),
            horizontalArrangement = Arrangement.spacedBy(LargeSpacing)
        ) {
            // Placeholder for the profile image
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.width(2.dp))

            Column {
                // Placeholder for the name
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(20.dp)
                        .background(Color.LightGray)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Placeholder for the bio
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(26.dp)
                        .background(Color.LightGray)
                )
            }
        }
    }
}