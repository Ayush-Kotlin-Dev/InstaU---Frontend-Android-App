package ayush.ggv.instau.presentation.screens.account.edit

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ayush.ggv.instau.R
import ayush.ggv.instau.presentation.components.CircleImage
import ayush.ggv.instau.presentation.components.CustomTextFields
import ayush.ggv.instau.ui.theme.ExtraLargeSpacing
import ayush.ggv.instau.ui.theme.LargeSpacing
import ayush.ggv.instau.ui.theme.SmallSpacing
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel = koinViewModel(),
    editProfileUiState: EditProfileUiState,
    onNameChange: (String) -> Unit,
    bioTextFieldValue: TextFieldValue,
    onBioChange: (TextFieldValue) -> Unit,
    onUploadButtonClick: () -> Unit,
    onUploadSuccess: () -> Unit,
    fetchProfile: () -> Unit
) {
    val context = LocalContext.current
    val storage = FirebaseStorage.getInstance()
    val coroutineScope = rememberCoroutineScope()

    var selectedImageUri by remember { mutableStateOf<String?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri?.toString()
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {


            editProfileUiState.profile != null -> {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box {
                        CircleImage(
                            modifier = modifier.size(100.dp),
                            imageUrl = selectedImageUri ?: editProfileUiState.profile.imageUrl
                            ?: "",
                            onClick = {}
                        )
                        IconButton(
                            onClick = { galleryLauncher.launch("image/*") },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(35.dp)
                                .background(
                                    color = MaterialTheme.colors.primary,
                                    shape = CircleShape
                                ),
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Edit,
                                contentDescription = null,
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                    Text(
                        text = "Update Profile Picture",
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(SmallSpacing))

                    Text(text = stringResource(id = R.string.username_text),modifier= Modifier.align(Alignment.Start), style = MaterialTheme.typography.subtitle2)
                    CustomTextFields(
                        value = editProfileUiState.profile.name,
                        onValueChange = onNameChange,
                        hint = R.string.username_hint,
                        modifier = Modifier.clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.height(SmallSpacing))

                    Text(text = stringResource(id = R.string.bio_text) ,modifier= Modifier.align(Alignment.Start), style = MaterialTheme.typography.subtitle2)
                    BioTextField(
                        value = bioTextFieldValue,
                        onValueChange = onBioChange,
                        modifier = Modifier.clip(RoundedCornerShape(8.dp))
                    )

                    Button(
                        onClick = {
                            viewModel.setLoadingState(true)
                            if (selectedImageUri != null) {
                                val imageUri = Uri.parse(selectedImageUri)
                                val storageRef =
                                    storage.reference.child("Profile_Images/${editProfileUiState.profile.name}_${imageUri.lastPathSegment}")

                                coroutineScope.launch {
                                    try {
                                        val uploadTask = storageRef.putFile(imageUri).await()
                                        val downloadUri = uploadTask.storage.downloadUrl.await()
                                        viewModel.updateImageUrl(downloadUri.toString())
                                    } catch (e: Exception) {
                                        viewModel.setLoadingState(false)
                                        Toast.makeText(
                                            context,
                                            "Failed to upload image: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@launch
                                    }
                                    onUploadButtonClick()
                                }
                            } else {
                                onUploadButtonClick()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(8.dp))
//                        enabled = !editProfileUiState.isLoading
                    ) {
                        Text(text = stringResource(id = R.string.upload_changes_text))
                    }
                }
            }

            editProfileUiState.errorMessage != null -> {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.could_not_load_profile),
                        style = MaterialTheme.typography.caption.copy(
                            textAlign = TextAlign.Center
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = fetchProfile,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(8.dp))

                    ) {
                        Text(text = stringResource(id = R.string.retry_button_text))
                    }
                }
            }
        }

        if (editProfileUiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }

    LaunchedEffect(Unit) {
        fetchProfile()
    }

    LaunchedEffect(editProfileUiState.uploadSuccess, editProfileUiState.errorMessage) {
        if (editProfileUiState.uploadSuccess) {
            Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT).show()
            onUploadSuccess()
        }
        if (editProfileUiState.errorMessage != null) {
            Toast.makeText(context, editProfileUiState.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun BioTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        textStyle = MaterialTheme.typography.body2,
        placeholder = {
            Text(
                text = stringResource(id = R.string.user_bio_hint),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )
        },
        shape = MaterialTheme.shapes.medium
    )
}