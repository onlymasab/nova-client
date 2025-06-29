package com.paandaaa.nova.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.paandaaa.nova.android.ui.screens.auth.AuthScreen
import com.paandaaa.nova.android.ui.screens.home.HomeScreen
import com.paandaaa.nova.android.ui.screens.onboarding.OnboardingScreen
import com.paandaaa.nova.android.ui.screens.splash.SplashScreen
import com.paandaaa.nova.android.viewmodel.AuthUiState
import com.paandaaa.nova.android.viewmodel.AuthViewModel
import androidx.compose.runtime.getValue
import com.paandaaa.nova.android.viewmodel.VoiceViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    voiceViewModel: VoiceViewModel
) {


    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                navController = navController,
            )
        }
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                navController = navController,
                onComplete = { navController.navigate(Screen.Auth.route) }
            )
        }
        composable(Screen.Auth.route) {
            AuthScreen(
                navController = navController,
                authViewModel = authViewModel,
                onSignInSuccess = {  navController.navigate(Screen.Home.route) }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                authViewModel = authViewModel,
                voiceViewModel = voiceViewModel,
                onSignOut = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}