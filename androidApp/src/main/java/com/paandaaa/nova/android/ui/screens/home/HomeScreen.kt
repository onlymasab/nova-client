package com.paandaaa.nova.android.ui.screens.home

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.util.UnstableApi
import coil3.compose.rememberAsyncImagePainter
import coil3.gif.GifDecoder
import coil3.request.ImageRequest
import com.paandaaa.nova.android.R
import com.paandaaa.nova.android.viewmodel.AuthViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.paandaaa.nova.android.ui.componenets.AIListeningAnimation
import com.paandaaa.nova.android.ui.componenets.AudioWaveformAnimation
import com.paandaaa.nova.android.ui.componenets.UserProfile
import com.paandaaa.nova.android.viewmodel.AuthUiState
import com.paandaaa.nova.android.viewmodel.VoiceState
import com.paandaaa.nova.android.viewmodel.VoiceViewModel
import com.paandaaa.nova.android.ui.componenets.VideoBackgroundPlayer
import com.paandaaa.nova.android.ui.componenets.OptionIconButton
import com.paandaaa.nova.android.ui.componenets.RecordingMicAnimation
import com.paandaaa.nova.android.ui.componenets.VoiceDotsAnimation

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    voiceViewModel: VoiceViewModel,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    onSignOut: () -> Unit
) {
    val voiceState by voiceViewModel.state.collectAsState()
    val authState by authViewModel.authState
    var isMuted by remember { mutableStateOf(false) }
    var isSpeakerOn by remember { mutableStateOf(true) }
   val context = LocalContext.current

    val micPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    var showPermissionDialog by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                voiceViewModel.startListening()
            } else {
                Toast.makeText(context, "Mic permission required", Toast.LENGTH_SHORT).show()
            }
        }
    )

    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = {
                showPermissionDialog = false
                isMuted = true
            },
            title = { Text(text = "Permissions Needed") },
            text = { Text("To use  microphone features, please grant both permissions.") },
            confirmButton = {
                TextButton(onClick = {
                    showPermissionDialog = false
                    micPermissionState.launchPermissionRequest()
                }) { Text(text = "Allow") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPermissionDialog = false
                    isMuted = true
                }) { Text(text = "Deny") }
            }
        )
    }



    Box(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {

        VideoBackgroundPlayer(rawResId = R.raw.model)
//
//        Column (
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            when (voiceState) {
//                is VoiceState.Idle -> Text("Tap to speak")
//                is VoiceState.Listening -> Text("Listening...")
//                is VoiceState.Result -> Text("Processing your speech...")
//                is VoiceState.ProcessingResponse -> Text("Nova is thinking...")
//                is VoiceState.Speaking -> Text("Nova is speaking...")
//                is VoiceState.Error -> Text("Error: ${(voiceState as VoiceState.Error).message}")
//            }
//
//            Button(
//                modifier = Modifier
//                    .padding(16.dp),
//                onClick = {
//                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
//                    //voiceViewModel.startListening() // Call the ViewModel function to start speech recognition
//                }
//            ) {
//                Text("Start Nova")
//            }
//
//        }



        Column {
            when (val state = authState) {
                is AuthUiState.Success -> {
                    val user = state.user
                    if (user != null) {
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .padding(vertical = 48.dp, horizontal = 24.dp)
                                .fillMaxSize(),
                        ) {
                            UserProfile(name = user.name , email = user.email, profilePictureUrl = user.profilePictureUrl)

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {


                                var isLoading by remember { mutableStateOf(false) }

                                // Update loading state based on voice state
                                LaunchedEffect(voiceState) {
                                    isLoading = voiceState is VoiceState.ProcessingResponse || voiceState is VoiceState.Result
                                }

                                Button(
                                    onClick = {
                                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                        voiceViewModel.startListening()
                                    },
                                    shape = CircleShape,
                                    colors = ButtonColors(
                                        containerColor = Color.White,
                                        contentColor = Color.Black,
                                        disabledContainerColor = Color.Gray,
                                        disabledContentColor = Color.Gray
                                    ),
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(CircleShape),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    if (isLoading) {
                                        CircularProgressIndicator(
                                            color = Color.Black,
                                            strokeWidth = 2.dp,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = "Start Nova",
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }

                                OptionIconButton(
                                    icon = if (isSpeakerOn) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                                    onClick = { isSpeakerOn = !isSpeakerOn },
                                    isActive = isSpeakerOn,
                                )
                                OptionIconButton(
                                    icon = if (isMuted) Icons.Default.Mic else Icons.Default.MicOff,
                                    onClick = { isMuted = !isMuted },
                                    isActive = !isMuted,
                                )
                                OptionIconButton(
                                    icon = Icons.Default.Close,
                                    onClick = {
                                        authViewModel.signOut()
                                        onSignOut()
                                    },
                                    isActive = true,
                                    isSignOut = true,
                                )
                            }
                        }
                    } else {
                        Text("No user data found.")
                    }
                }
                is AuthUiState.Loading -> CircularProgressIndicator()
                is AuthUiState.Error -> Text("Error: ${state.message}")
                AuthUiState.Idle -> Text("Not signed in.")
            }
        }

    }
}








