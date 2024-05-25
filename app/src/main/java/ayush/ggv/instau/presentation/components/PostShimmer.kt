package ayush.ggv.instau.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.items
import ayush.ggv.instau.R
import ayush.ggv.instau.ui.theme.DarkGray
import ayush.ggv.instau.ui.theme.LargeSpacing
import ayush.ggv.instau.ui.theme.LightGray
import ayush.ggv.instau.ui.theme.MediumSpacing
import ayush.ggv.instau.ui.theme.SMALL_PADDING
import ayush.ggv.instau.ui.theme.ShimmerLightGray

private val ShimmerAnimationDefinition = infiniteRepeatable<Float>(
    animation = tween(
        durationMillis = 1300,
        delayMillis = 300,
        easing = LinearEasing
    ),
    repeatMode = RepeatMode.Restart
)
@Composable
fun ShimmerAnimation(
    colors: List<Color>,
    content: @Composable() () -> Unit
) {
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 2000f,
        animationSpec = ShimmerAnimationDefinition
    )

    val brush = Brush.linearGradient(
        colors = colors,
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim - 1000f, 0f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = brush)
    ) {
        content()
    }
}
@Composable
fun ShimmerEffect(){
    LazyColumn(
        contentPadding = PaddingValues(all = SMALL_PADDING),
        verticalArrangement = Arrangement.spacedBy(SMALL_PADDING)
    ) {
        items(5){
            ShimmerPostListItemPlaceholder()
        }

    }
}
@Composable
fun ShimmerPostListItemPlaceholder() { // make it adapt for dark theme also
    val isDarkTheme = isSystemInDarkTheme()
    val colors = if (isDarkTheme) {
        listOf(Color.DarkGray.copy(0.9f), Color.DarkGray.copy(0.2f), Color.DarkGray.copy(0.9f))
    } else {
        listOf(Color.LightGray.copy(0.9f), Color.LightGray.copy(0.2f), Color.LightGray.copy(0.9f))
    }
    val placeholderColor = if (isDarkTheme) Color.DarkGray else Color.LightGray

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = if (isSystemInDarkTheme()) Color.Black else ShimmerLightGray,
        shape = RoundedCornerShape(20.dp)
    ) {
        ShimmerAnimation(colors = colors) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Placeholder for the header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = LargeSpacing,
                            vertical = MediumSpacing
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Placeholder for the profile image
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(placeholderColor)
                    )

                    // Placeholder for the name
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(20.dp)
                            .background(placeholderColor)
                    )

                    Spacer(modifier = Modifier.width(100.dp))

                    // Placeholder for the more options icon
                    Icon(
                        painter = painterResource(id = R.drawable.round_more_horiz_24),
                        contentDescription = null,
                        tint = placeholderColor,
                    )
                }

                // Placeholder for the image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.0f)
                        .background(placeholderColor)
                )

                // Placeholder for the like row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Placeholder for the like icon
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(placeholderColor)
                    )

                    // Placeholder for the likes count
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(20.dp)
                            .background(placeholderColor)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Placeholder for the comment icon
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(placeholderColor)
                    )

                    // Placeholder for the comments count
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(20.dp)
                            .background(placeholderColor)
                    )
                }

                // Placeholder for the caption
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(26.dp)
                        .background(placeholderColor)
                )
            }
        }
    }
}