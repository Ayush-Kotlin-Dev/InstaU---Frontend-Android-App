package ayush.ggv.instau.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ayush.ggv.instau.common.fakedata.Comment
import ayush.ggv.instau.ui.theme.LargeSpacing
import ayush.ggv.instau.ui.theme.MediumSpacing
import ayush.ggv.instau.R
import ayush.ggv.instau.common.fakedata.sampleComments
import ayush.ggv.instau.ui.theme.DarkGray
import ayush.ggv.instau.ui.theme.LightGray
import ayush.ggv.instau.ui.theme.SocialAppTheme

@Composable
fun CommentListItem(
    modifier: Modifier = Modifier,
    comment: Comment,
    onProfileClick: (Int) -> Unit,
    onMoreIconClick: () -> Unit
) {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(LargeSpacing),
            horizontalArrangement = Arrangement.spacedBy(MediumSpacing)
        ) {
            CircleImage(
                imageUrl = comment.authorImageUrl,
                modifier = modifier.size(30.dp)
            ) {
                onProfileClick(comment.authorId)
            }


            Column(
                modifier = modifier.weight(1f)
            ) {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MediumSpacing)
                ) {
                    Text(
                        text = comment.authorName,
                        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                        modifier = modifier.alignByBaseline()
                    )

                    Text(
                        text = comment.date,
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
                        contentDescription = null ,
                        tint = if(MaterialTheme.colors.isLight)
                            Color.LightGray
                        else Color.DarkGray,
                        modifier = modifier
                            .clickable {
                                onMoreIconClick()
                            }
                    )
                }
                Text(
                    text = comment.comment,
                    style = MaterialTheme.typography.body1,
                    modifier = modifier.padding(top = MediumSpacing)
                )

            }
        }


    }

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES , showBackground = true)
fun PreviewCommentListItem() {
    SocialAppTheme {
        Surface(color = MaterialTheme.colors.surface) {
            CommentListItem(
                comment = sampleComments.first(),
                onProfileClick = {},
                onMoreIconClick = {}
            )
        }
    }
}