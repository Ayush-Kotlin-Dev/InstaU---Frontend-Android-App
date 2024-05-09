package ayush.ggv.instau.presentation.screens.account.edit

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ayush.ggv.instau.R
import ayush.ggv.instau.presentation.components.CircleImage
import ayush.ggv.instau.presentation.components.CustomTextFields
import ayush.ggv.instau.ui.theme.ButtonHeight
import ayush.ggv.instau.ui.theme.ExtraLargeSpacing
import ayush.ggv.instau.ui.theme.LargeSpacing
import com.google.firebase.storage.FirebaseStorage
import org.koin.androidx.compose.koinViewModel
import java.time.format.TextStyle

@Composable

fun EditProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel = koinViewModel(), // Add this parameter
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

    // State variable to hold the selected image URI
    var selectedImageUri by remember { mutableStateOf("") }

    // Gallery launcher
    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            // Set the selected image URI to the state variable
            selectedImageUri = uri.toString()
        }

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            editProfileUiState.isLoading -> {
                // Loading
            }

            editProfileUiState.profile != null -> {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .background(
                            color = if (isSystemInDarkTheme()) {
                                MaterialTheme.colors.background
                            } else {
                                MaterialTheme.colors.surface
                            }
                        )
                        .padding(ExtraLargeSpacing),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(LargeSpacing)

                ) {
                    Box() {
                        // Profile Image
                        CircleImage(
                            modifier = modifier.size(100.dp),
                            imageUrl = if (selectedImageUri.isNotEmpty()) selectedImageUri else editProfileUiState.profile.imageUrl ?: "",
                            onClick = {}
                        )
                        IconButton(
                            onClick = { galleryLauncher.launch("image/*") },
                            modifier = modifier
                                .align(Alignment.BottomEnd)
                                .size(40.dp)
                                .background(
                                    color = MaterialTheme.colors.primary,
                                    shape = CircleShape
                                )
                                .shadow(elevation = 5.dp, shape = RoundedCornerShape(percent = 25))
                                .background(
                                    color = MaterialTheme.colors.surface,
                                    shape = RoundedCornerShape(percent = 25)
                                )

                        ) {

                            Icon(
                                imageVector = Icons.Rounded.Edit,
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }

                    Spacer(modifier = modifier.height(LargeSpacing))

                    CustomTextFields(
                        value = editProfileUiState.profile.name,
                        onValueChange = onNameChange,
                        hint = R.string.username_hint
                    )
                    BioTextField(
                        value = bioTextFieldValue,
                        onValueChange = onBioChange
                    )
                    Button(
                        onClick = {
                            val imageUri = Uri.parse(selectedImageUri)
                            val storageRef =
                                storage.reference.child("Profile_Images/${editProfileUiState.profile.name}_${imageUri.lastPathSegment}")
                            val uploadTask = storageRef.putFile(imageUri)
                            uploadTask.addOnSuccessListener {
                                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                                    viewModel.updateImageUrl(downloadUri.toString()) // Call the function on the ViewModel instance
                                    onUploadButtonClick()
                                }
                            }
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .height(ButtonHeight),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp
                        ),
                        shape = MaterialTheme.shapes.medium,
                    ) {
                        Text(
                            text = stringResource(id = R.string.upload_changes_text),
                            style = MaterialTheme.typography.button
                        )

                    }


                }
            }

            editProfileUiState.errorMessage != null -> {
                Column {
                    Text(
                        text = stringResource(id = R.string.could_not_load_profile),
                        style = MaterialTheme.typography.caption.copy(
                            textAlign = TextAlign.Center
                        ),
                    )
                    Spacer(modifier = modifier.height(LargeSpacing))
                    Button(
                        onClick = {
                            fetchProfile()
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .height(ButtonHeight),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp
                        ),
                        shape = MaterialTheme.shapes.medium,
                    ) {
                        Text(
                            text = stringResource(id = R.string.retry_button_text),
                            style = MaterialTheme.typography.button
                        )
                    }

                }
            }
        }
        if (editProfileUiState.isLoading) {
            CircularProgressIndicator()
        }
    }
    LaunchedEffect(
        key1 = Unit,
        block = {
            fetchProfile()
        }
    )
    LaunchedEffect(
        key1 = editProfileUiState.uploadSuccess,
        key2 = editProfileUiState.errorMessage,
        block = {
            if (editProfileUiState.uploadSuccess) {
                Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT).show()

                onUploadSuccess()
            }
            if (editProfileUiState.profile != null && editProfileUiState.errorMessage != null) {
                Toast.makeText(context, editProfileUiState.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    )




}

@Composable
fun BioTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit
) {
    TextField(
        value = value,
        onValueChange = {
            onValueChange(
                TextFieldValue(
                    text = it.text,
                    selection = TextRange(it.text.length)
                )
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = if (isSystemInDarkTheme()) MaterialTheme.colors.surface else MaterialTheme.colors.onSurface.copy(
                alpha = 0.1f
            ),
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
        shape = MaterialTheme.shapes.medium,
    )

}