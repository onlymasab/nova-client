package com.paandaaa.nova.android.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.paandaaa.nova.android.viewmodel.AuthViewModel
import com.paandaaa.nova.android.viewmodel.AuthUiState
import com.paandaaa.nova.android.BuildConfig
import com.paandaaa.nova.android.R
import androidx.compose.runtime.getValue // Required for 'by' delegation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun AuthScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    onSignInSuccess: () -> Unit
) {
    val authState by authViewModel.authState
    val context = LocalContext.current
    Scaffold (
        containerColor = Color.White
    ) {
        innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Image(
                painter = painterResource(id = R.drawable.auth_cover),
                contentDescription = "Auth Cover Image",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Hello,\nWelcome Back!",
                    style = TextStyle(
                        fontSize = 31.4.sp,
                        lineHeight = 40.sp,
                        fontWeight = FontWeight(600),
                        color = Color(0xFF242424),
                        textAlign = TextAlign.Center,
                        letterSpacing = 1.26.sp,
                    )
                )

                Text(
                    text = "Please choose your social provider to\naccess your AI Friend ",
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF656565),
                        textAlign = TextAlign.Center,
                    )
                )
            }

            when (authState) {
                is AuthUiState.Idle -> {
                    Column (
                        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        SignInButton (
                            onClick = { authViewModel.signInWithGoogle(BuildConfig.GOOGLE_WEB_CLIENT_ID) },
                            logo = R.drawable.google_logo,
                            provider = "Continue with Google"
                        )
                        SignInButton (
                            onClick = { Toast.makeText(context, "Not Available!", Toast.LENGTH_SHORT).show() },
                            logo = R.drawable.apple_logo,
                            provider = "Continue with Apple"
                        )
                    }

                }

                is AuthUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is AuthUiState.Success -> {
                    Text("Signed in successfully!")
                     onSignInSuccess() // You probably want this here
                }

                is AuthUiState.Error -> {
                    val message = (authState as AuthUiState.Error).message
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    Column (
                        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        SignInButton (
                            onClick = { authViewModel.signInWithGoogle(BuildConfig.GOOGLE_WEB_CLIENT_ID) },
                            logo = R.drawable.google_logo,
                            provider = "Continue with Google"
                        )
                        SignInButton (
                            onClick = { Toast.makeText(context, "Not Available!", Toast.LENGTH_SHORT).show() },
                            logo = R.drawable.apple_logo,
                            provider = "Continue with Apple"
                        )
                    }
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )
        }
    }



}

@Composable
fun SignInButton(onClick: () -> Unit, logo: Int, provider: String) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.LightGray,
            contentColor = Color.Black,
        ),
        shape = CircleShape,
        modifier = Modifier
            .size(
                width = 320.dp,
                height = 44.dp,
            )
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = logo),
                contentDescription = "Google Icon",
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = provider,
                modifier = Modifier
                    .width(180.dp)
                )
        }
    }
}