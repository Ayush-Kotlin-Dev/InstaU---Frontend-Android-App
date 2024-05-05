package ayush.ggv.instau.presentation.screens.add_post

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ayush.ggv.instau.R
import ayush.ggv.instau.presentation.components.CustomTextFields
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun AddPostScreen(
    addPostUiState: AddPostViewModel.AddPostUiState,
    captionText: String,
    onCaptionChange: (String) -> Unit,
    initialSelectedImageUri: String?,
    userId: Long,
    onUploadPost: (String, String , String , Long ) -> Unit,
    onUploadSuccess: () -> Unit,
    token : String

) {
    val storage = FirebaseStorage.getInstance()
    var imageSelected by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf(initialSelectedImageUri) }
    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedImageUri = uri.toString()
            imageSelected = uri != null
        }
//    LaunchedEffect(Unit) {
//        galleryLauncher.launch("image/*")
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .aspectRatio(ratio = 1.0f)
                .padding(4.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            if (imageSelected) {
                Image(
                    painter = rememberAsyncImagePainter(model = selectedImageUri),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxSize(),
                        contentScale = ContentScale.Crop
                )
            } else {
                   Image(
                       painter =
                       if (MaterialTheme.colors.isLight) {
                       painterResource(id = R.drawable.light_image_place_holder)
                   } else {
                       painterResource(id = R.drawable.dark_image_place_holder)
                   } , contentDescription =  null  , modifier = Modifier
                           .fillMaxSize()
                           .clickable { galleryLauncher.launch("image/*") }
                   )
                    Text(
                        text = "No image selected, Click here to select an image",
                        color = if (MaterialTheme.colors.isLight) Color.Black.copy(alpha = 0.5f)  else Color.White.copy(alpha = 0.4f),
                        modifier = Modifier.align(Alignment.Center),
                    )


            }

        }
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextFields(
            value = captionText,
            onValueChange = onCaptionChange,
            hint = R.string.caption,
            keyboardType = KeyboardType.Text,
            isSingleLine = false,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(16.dp))
                .height(100.dp),
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            onClick = {
                val imageUri = Uri.parse(selectedImageUri)
                val storageRef =
                    storage.reference.child("images/${userId}_${imageUri.lastPathSegment}")
                val uploadTask = storageRef.putFile(imageUri)
                uploadTask.addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        onUploadPost(downloadUri.toString(), captionText , token , userId )
                    }
                }

            },
        ) {
            Text(text = "Post")
        }

    }
}
