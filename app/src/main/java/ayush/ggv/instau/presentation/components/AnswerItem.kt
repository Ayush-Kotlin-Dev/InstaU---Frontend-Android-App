package ayush.ggv.instau.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ayush.ggv.instau.data.dateTimeFormat
import ayush.ggv.instau.model.qna.Answer
import coil.compose.rememberAsyncImagePainter
@Composable
fun AnswerBubble(
    answer: Answer,
    isSender: Boolean,
) {
    val radius =
        if (isSender) RoundedCornerShape(
            topStart = 16.dp,
            bottomStart = 16.dp,
            topEnd = 0.dp,
            bottomEnd = 16.dp
        ) else RoundedCornerShape(
            topStart = 0.dp,
            bottomStart = 16.dp,
            topEnd = 16.dp,
            bottomEnd = 16.dp
        )
    val (formattedDate, formattedTime) = dateTimeFormat(answer.createdAt)
    Column(
        modifier = Modifier
            .padding(bottom = 24.dp)
    ) {
        Row {
            if (isSender.not()) {
                AvatarHeadQna(formattedTime)
            }
            Text(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .weight(0.8f)
                    .wrapContentSize(align = if (isSender) Alignment.CenterEnd else Alignment.CenterStart)
                    .background(
                        color = if (isSender)
                            MaterialTheme.colors.secondary
                        else
                            MaterialTheme.colors.primary,
                        shape = radius
                    )
                    .padding(12.dp),
                text = answer.answer,
                color = if (isSender)
                    MaterialTheme.colors.onSecondary
                else
                    MaterialTheme.colors.onPrimary,
                fontWeight = FontWeight.Normal
            )
            if (isSender) {
                AvatarHeadQna(formattedTime)
            }
        }
    }
}
@Composable
fun AvatarHeadQna(
    time: String,
) {
    val avatar =
        "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcTK6zxVFdnRvs-c5qBxap3VCBLf87lGp5EKB0eJRmjp1UvuoKGRO_Qmj8N1jPOcAaxL7lEQOQGDVc9sILOrCmXUZQjBU4FYkExFNRHGukU"
    Column(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(35.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(color = MaterialTheme.colors.onBackground),
            painter = rememberAsyncImagePainter(model = avatar),
            contentDescription = "Friend avatar"
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = time,
            color = Color.Black,
            style = MaterialTheme.typography.body2
                .copy(
                    fontSize = 10.sp,
                    color = MaterialTheme.colors.onBackground
                )
        )
    }
}
