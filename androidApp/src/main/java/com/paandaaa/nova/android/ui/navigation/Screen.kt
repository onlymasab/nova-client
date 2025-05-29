package com.paandaaa.nova.android.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Auth: Screen("auth")
    object Home: Screen("home")
}