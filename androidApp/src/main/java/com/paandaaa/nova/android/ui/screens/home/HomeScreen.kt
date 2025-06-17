package com.paandaaa.nova.android.ui.screens.home

import android.Manifest
import android.util.Log
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import coil3.compose.rememberAsyncImagePainter
import coil3.gif.GifDecoder
import coil3.request.ImageRequest
import com.paandaaa.nova.android.R
import com.paandaaa.nova.android.viewmodel.AuthViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.crossfade
import coil3.request.transformations
import coil3.size.Scale
import coil3.transform.CircleCropTransformation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.paandaaa.nova.android.viewmodel.AuthUiState
import com.paandaaa.nova.android.viewmodel.VoiceViewModel


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    onSignOut: () -> Unit
) {
    // State variables for UI controls
    val authState by authViewModel.authState
    var isMuted by remember { mutableStateOf(false) }
    var isCameraOn by remember { mutableStateOf(true) }
    var isSpeakerOn by remember { mutableStateOf(true) }
    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_FRONT_CAMERA) }
    val context = LocalContext.current

// Camera Permission
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    var showPermissionDialog by remember { mutableStateOf(false) }
    // 👇 Check permission on launch
    LaunchedEffect(cameraPermissionState.status) {
        if (!cameraPermissionState.status.isGranted) {
            showPermissionDialog = true
        }
    }

// 👇 UI Dialog to explain camera permission
    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = {
                showPermissionDialog = false
                isMuted = true
                isCameraOn = false
            },
            title = {
                Text(text = "Camera Permission Needed")
            },
            text = {
                Text("To use camera and microphone, please grant camera permission.")
            },
            confirmButton = {
                TextButton(onClick = {
                    showPermissionDialog = false
                    cameraPermissionState.launchPermissionRequest()
                }) {
                    Text(text = "Allow")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPermissionDialog = false
                    isMuted = true
                    isCameraOn = false
                }) {
                    Text(text = "Deny")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
//        GifImage(
//            url = R.drawable.wave_model,
//            modifier = Modifier.fillMaxSize()
//        )

        VoiceScreen()

        Column {
            when (authState) {
                is AuthUiState.Success -> {
                    val user = (authState as AuthUiState.Success).user

                    if (user != null) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier
                                .padding(top = 32.dp, start = 24.dp, end = 24.dp)
                                .fillMaxWidth(),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
                            ) {
                                UserAvatar(
                                    url = user.profilePictureUrl,
                                    modifier = Modifier
                                        .clip(shape = CircleShape)
                                        .background(color = Color(0xFFF3F49B))
                                        .size(56.dp)
                                )

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(2.dp),
                                    horizontalAlignment = Alignment.Start,
                                ) {
                                    Text(
                                        text = user.name,
                                        style = TextStyle(
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = user.email,
                                        style = TextStyle(
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = Color.Gray
                                        )
                                    )
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Speaker button
                                OptionIconButton(
                                    icon = if ( isSpeakerOn ) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                                    onClick = { isSpeakerOn = !isSpeakerOn },
                                    isActive = isSpeakerOn
                                )

                                // Camera on/off button
                                OptionIconButton(
                                    icon = if ( isCameraOn ) Icons.Default.Videocam else Icons.Default.VideocamOff,
                                    onClick = { isCameraOn = !isCameraOn },
                                    isActive = isCameraOn
                                )

                                // Camera flip button
                                OptionIconButton(
                                    icon = Icons.Default.Cameraswitch,
                                    onClick = {
                                        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
                                            CameraSelector.DEFAULT_BACK_CAMERA
                                        } else {
                                            CameraSelector.DEFAULT_FRONT_CAMERA
                                        }
                                    },
                                    isActive = true
                                )

                                // Mic mute/unmute button
                                OptionIconButton(
                                    icon = if (isMuted) Icons.Default.Mic else Icons.Default.MicOff,
                                    onClick = { isMuted = !isMuted },
                                    isActive = !isMuted,
                                    text = if (isMuted) "Unmute" else "Mute"
                                )

                                // Sign out button
                                OptionIconButton(
                                    icon = Icons.Default.Close,
                                    onClick = {
                                        authViewModel.signOut()
                                        onSignOut() // Navigate back to auth screen
                                    },
                                    isActive = true,
                                    isSignOut = true
                                )
                            }
                        }
                    } else {
                        Text("No user data found.")
                    }
                }
                is AuthUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is AuthUiState.Error -> {
                    val message = (authState as AuthUiState.Error).message
                    Text("Error: $message")
                }
                AuthUiState.Idle -> {
                    Text("Not signed in.")
                }
                AuthUiState.Loading -> {}
            }
        }

        if (cameraPermissionState.status.isGranted && isCameraOn) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 24.dp, end = 24.dp)
                    .size(width = 140.dp, height = 160.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(16.dp))
                    .background(Color.Gray, RoundedCornerShape(16.dp))
            ) {
                CameraPreviewView(cameraSelector = cameraSelector)
            }
        } else if (!isCameraOn) {
            // Show placeholder when camera is off
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 24.dp, end = 24.dp)
                    .size(width = 140.dp, height = 160.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(16.dp))
                    .background(Color.DarkGray, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Camera Off",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun OptionIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    isActive: Boolean = false,
    isSignOut: Boolean = false,
    text: String? = null
) {
    val containerColor = when {
        isSignOut -> Color.Red
        isActive -> Color.White
        else -> Color.White.copy(alpha = 1f)
    }

    val contentColor = when {
        isSignOut -> Color.White
        isActive -> Color.Black
        else -> Color.Gray
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconButton(
            onClick = onClick,
            colors = IconButtonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = if (isActive) Color.White else Color.Gray,
                disabledContentColor = if (isActive) Color.White else Color.Gray,
            ),
            modifier = Modifier.size(52.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Button Icon",
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = text ?: when (icon) {
                Icons.Default.Mic -> if (isActive) "Mute" else "Unmute"
                Icons.Default.Videocam -> if (isActive) "Camera" else "Camera Off"
                Icons.AutoMirrored.Filled.VolumeUp -> "Speaker"
                Icons.Default.Cameraswitch -> "Switch"
                else -> "Sign out"
            },
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = if (isActive) Color.Black else Color.Gray
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun CameraPreviewView(cameraSelector: CameraSelector) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build()
                preview.setSurfaceProvider(previewView.surfaceProvider)

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview
                    )
                } catch (exc: Exception) {
                    Log.e("CameraPreviewView", "Camera binding failed", exc)
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun GifImage(url: Int, modifier: Modifier = Modifier) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(url)
            .decoderFactory(GifDecoder.Factory())
            .build()
    )

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

@Composable
fun UserAvatar(url: String?, modifier: Modifier = Modifier) {
    AsyncImage(
        model = url,
        contentDescription = "User Profile Picture",
        modifier = modifier
            .size(100.dp)
            .clip(CircleShape)
            .border(2.dp, Color(0xFFF3F49B), CircleShape),
        placeholder = painterResource(R.drawable.user_avatar),  // fallback image
        error = painterResource(R.drawable.user_avatar),        // fallback if failed
        contentScale = ContentScale.Crop
    )
}



@Composable
fun VoiceScreen(viewModel: VoiceViewModel = hiltViewModel()) {
    Column {
        Text(text = viewModel.result)
        Button(onClick = { viewModel.startListening() }) {
            Text("🎙️ Say 'Hey Nova'")
        }
    }
}