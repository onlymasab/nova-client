package com.paandaaa.nova.android.domain.usecase.auth

// File: AuthUseCases.kt

data class AuthUseCases(
    val signInWithGoogle: SignInWithGoogleUseCase,
    val isAuthenticated: IsAuthenticatedUseCase,
    val getCurrentUser: GetCurrentUserUseCase,
    val signOut: SignOutUseCase,
    val getIdToken: GetIdTokenUseCase
)