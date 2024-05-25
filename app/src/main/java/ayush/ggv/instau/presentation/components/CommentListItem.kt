package ayush.ggv.instau.presentation.components

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ayush.ggv.instau.ui.theme.LargeSpacing
import ayush.ggv.instau.ui.theme.MediumSpacing
import ayush.ggv.instau.R
import ayush.ggv.instau.model.PostComment
import ayush.ggv.instau.ui.theme.DarkGray
import ayush.ggv.instau.ui.theme.LightGray
import ayush.ggv.instau.ui.theme.SocialAppTheme
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun CommentListItem(
    modifier: Modifier = Modifier,
    comment: PostComment,
    onProfileClick: (Long) -> Unit,
    onDeleteClick: () -> Unit
) {
    fun formatTimeAgo(time: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        val dateTime = LocalDateTime.parse(time, formatter)
        val zonedDateTime = dateTime.atZone(ZoneId.systemDefault())
        val now = ZonedDateTime.now()

        val diff = ChronoUnit.SECONDS.between(zonedDateTime, now)

        return when {
            diff < 60 -> "$diff seconds ago"
            diff < 3600 -> "${diff / 60} minutes ago"
            diff < 86400 -> "${diff / 3600} hours ago"
            diff < 604800 -> "${diff / 86400} days ago"
            else -> "${diff / 604800} weeks ago"
        }
    }


    // Format the LocalDateTime object into a "time ago" string
    val timeAgo = formatTimeAgo(comment.createdAt)

    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(LargeSpacing),
        horizontalArrangement = Arrangement.spacedBy(MediumSpacing)
    ) {
        CircleImage(
            imageUrl = comment.userImageUrl ?: "",
            modifier = modifier.size(30.dp)
        ) {
            onProfileClick(comment.userId)
        }


        Column(
            modifier = modifier.weight(1f)
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MediumSpacing)
            ) {
                Text(
                    text = comment.userName,
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                    modifier = modifier.alignByBaseline()
                )

                Text(
                    text = timeAgo,
                    style = MaterialTheme.typography.body2.copy(
                        color = MaterialTheme.colors.onSurface.copy(
                            alpha = 0.6f
                        )
                    ),
                    color = if (MaterialTheme.colors.isLight) {
                        LightGray
                    } else {
                        DarkGray
                    },
                    modifier = modifier
                        .alignByBaseline()
                        .weight(1f)
                )


                Icon(
                    painter = painterResource(id = R.drawable.round_more_horiz_24),
                    contentDescription = null,
                    tint = if (MaterialTheme.colors.isLight)
                        Color.LightGray
                    else Color.DarkGray,
                    modifier = modifier
                        .clickable {
                            expanded = true
                        }
                )
            }
            // Three-dot menu
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(onClick = { onDeleteClick()}) {
                    Text("Delete")
                }
                DropdownMenuItem(onClick = { }) {
                    Text("Copy")
                }
            }
            Text(
                text = comment.content,
                style = MaterialTheme.typography.body1,
                modifier = modifier.padding(top = MediumSpacing)
            )
        }
    }


}
