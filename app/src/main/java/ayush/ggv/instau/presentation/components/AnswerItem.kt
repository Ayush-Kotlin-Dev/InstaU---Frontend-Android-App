package ayush.ggv.instau.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ayush.ggv.instau.R
import ayush.ggv.instau.data.dateTimeFormat
import ayush.ggv.instau.model.qna.Answer
import coil.compose.rememberAsyncImagePainter
@Composable
fun AnswerBubble(
    answer: Answer,
    isSender: Boolean,
) {
    val radius = if (isSender) {
        RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp, topEnd = 4.dp, bottomEnd = 16.dp)
    } else {
        RoundedCornerShape(topStart = 4.dp, bottomStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp)
    }

    val (formattedDate, formattedTime) = dateTimeFormat(answer.createdAt)
    val bubbleColor = if (isSender) {
        MaterialTheme.colors.secondary.copy(alpha = 0.7f)
    } else {
        MaterialTheme.colors.primary.copy(alpha = 0.7f)
    }
    val textColor = if (isSender) {
        MaterialTheme.colors.onSecondary
    } else {
        MaterialTheme.colors.onPrimary
    }

    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (isSender) Arrangement.End else Arrangement.Start
        ) {
            if (!isSender) {
                AvatarHeadQna(formattedTime, answer.authorName ?: "")
                Spacer(modifier = Modifier.width(8.dp))
            }

            Column(
                modifier = Modifier
                    .weight(0.8f)
                    .background(
                        color = bubbleColor,
                        shape = radius
                    )
                    .clip(radius)
                    .shadow(2.dp, radius)
                    .padding(12.dp)
            ) {
                Text(
                    text = answer.authorName ?: "",
                    style = MaterialTheme.typography.caption,
                    color = textColor.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = answer.answer,
                    color = textColor,
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formattedTime,
                    style = MaterialTheme.typography.caption,
                    color = textColor.copy(alpha = 0.5f),
                    modifier = Modifier.align(if (isSender) Alignment.End else Alignment.Start)
                )
            }

            if (isSender) {
                Spacer(modifier = Modifier.width(8.dp))
                AvatarHeadQna(formattedTime, answer.authorName ?: "")
            }
        }
    }
}

@Composable
fun AvatarHeadQna(
    time: String,
    authorName: String
) {
    val avatar = "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcTK6zxVFdnRvs-c5qBxap3VCBLf87lGp5EKB0eJRmjp1UvuoKGRO_Qmj8N1jPOcAaxL7lEQOQGDVc9sILOrCmXUZQjBU4FYkExFNRHGukU"

    Column(
        modifier = Modifier.width(60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.surface)
                .border(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f), CircleShape)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = avatar,
                    placeholder = painterResource(R.drawable.person_circle_icon)
                ),
                contentDescription = "Avatar for $authorName",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = authorName.split(" ").firstOrNull() ?: "",
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
@Preview
fun AnswerBubblePreview() {
    AnswerBubble(
        answer = Answer(
            id = 1,
            questionId = 1,
            authorId = 1,
            authorName = "John Doe",
            answer = "This is a sample answer",
            createdAt = ""
        ),
        isSender = true
    )
}
