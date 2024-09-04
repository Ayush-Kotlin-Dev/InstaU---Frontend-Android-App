package ayush.ggv.instau.presentation.screens.account.edit

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
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
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
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
    onUploadButtonClick: (imageByteArray: ByteArray) -> Unit,
    onUploadSuccess: () -> Unit,
    fetchProfile: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var selectedImageUri by remember { mutableStateOf<String?>(null) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

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
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProfilePictureSection(
                        imageUrl = selectedImageUri ?: editProfileUiState.profile.imageUrl ?: "",
                        onGalleryClick = { galleryLauncher.launch("image/*") },
                        onDeleteClick = { showDeleteConfirmDialog = true }
                    )

                    NameSection(
                        name = editProfileUiState.profile.name,
                        onNameChange = onNameChange
                    )

                    BioSection(
                        bioTextFieldValue = bioTextFieldValue,
                        onBioChange = onBioChange
                    )

                    Button(
                        onClick = {
                            viewModel.setLoadingState(true)
                            if (selectedImageUri != null) {
                                coroutineScope.launch {
                                    val resolver = context.contentResolver
                                    val imageBytes =
                                        resolver.openInputStream(Uri.parse(selectedImageUri))
                                            ?.readBytes()
                                    if (imageBytes != null) {
                                        onUploadButtonClick(imageBytes)
                                    } else {
                                        viewModel.setLoadingState(false)
                                        Toast.makeText(
                                            context,
                                            "Failed to read image",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {
                                viewModel.setLoadingState(false)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Text(text = stringResource(id = R.string.upload_changes_text))
                    }
                }
            }
            editProfileUiState.errorMessage != null -> {
                ErrorSection(
                    message = stringResource(id = R.string.could_not_load_profile),
                    onRetry = fetchProfile
                )
            }
        }

        if (editProfileUiState.isLoading) {
            CircularProgressIndicator()
        }

        if (showDeleteConfirmDialog) {
            DeleteConfirmationDialog(
                onConfirm = {
                    // TODO: Implement delete profile picture logic
                    showDeleteConfirmDialog = false
                },
                onDismiss = { showDeleteConfirmDialog = false }
            )
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
fun ProfilePictureSection(
    imageUrl: String,
    onGalleryClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Box {
        CircleImage(
            modifier = Modifier.size(100.dp),
            imageUrl = imageUrl,
            onClick = {}
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 8.dp)
        ) {
            IconButton(
                onClick = onGalleryClick,
                modifier = Modifier
                    .size(35.dp)
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = "Change picture",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier
                    .size(35.dp)
                    .background(
                        color = MaterialTheme.colors.error,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = "Delete picture",
                    tint = MaterialTheme.colors.onError
                )
            }
        }
    }
}

@Composable
fun NameSection(
    name: String,
    onNameChange: (String) -> Unit
) {
    Text(
        text = stringResource(id = R.string.username_text),
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.subtitle2
    )
    CustomTextFields(
        value = name,
        onValueChange = onNameChange,
        hint = R.string.username_hint,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
    )
}

@Composable
fun BioSection(
    bioTextFieldValue: TextFieldValue,
    onBioChange: (TextFieldValue) -> Unit
) {
    Text(
        text = stringResource(id = R.string.bio_text),
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.subtitle2
    )
    BioTextField(
        value = bioTextFieldValue,
        onValueChange = onBioChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(8.dp))
    )

}

@Composable
fun ErrorSection(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.caption.copy(
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onRetry,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Text(text = stringResource(id = R.string.retry_button_text))
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Profile Picture") },
        text = { Text("Are you sure you want to delete your profile picture?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun BioTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.body1,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = modifier
    )
}
