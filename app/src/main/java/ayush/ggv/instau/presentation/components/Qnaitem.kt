package ayush.ggv.instau.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ayush.ggv.instau.model.qna.QuestionWithAnswer
import ayush.ggv.instau.util.coloredShadow

@Composable
fun QuestionItem(
    questionWithAnswer: QuestionWithAnswer,
    onItemClick: (QuestionWithAnswer) -> Unit
) {
    val question = questionWithAnswer.question
    val questionRaiser = questionWithAnswer.authorName
    val recentAnswer = questionWithAnswer.mostRecentAnswer

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(205.dp)
            .coloredShadow(
                color = Color.Black,
                alpha = 0.1f,
                shadowRadius = 25.dp
            )
        .clickable(onClick = { onItemClick(questionWithAnswer) })
            .padding(8.dp)
            .clip(RoundedCornerShape(7.dp)),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Asked by: $questionRaiser",
                style = MaterialTheme.typography.caption,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = question,
                style = MaterialTheme.typography.h6,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (recentAnswer != null) {
                Text(
                    text = "Answer: $recentAnswer",
                    style = MaterialTheme.typography.body1,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                Text(
                    text = "No answers yet , be the first to answer!",
                    style = MaterialTheme.typography.body1,
                    color = Color.Gray
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = "Expand",
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}