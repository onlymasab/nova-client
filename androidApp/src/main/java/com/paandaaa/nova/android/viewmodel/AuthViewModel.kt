package com.paandaaa.nova.android.viewmodel

import android.app.Activity
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.paandaaa.nova.android.domain.model.UserModel
import com.paandaaa.nova.android.domain.usecase.auth.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _authState = mutableStateOf<AuthUiState>(AuthUiState.Idle)
    val authState: State<AuthUiState> = _authState

    fun signInWithGoogle(activity: Activity, serverClientId: String) {
        viewModelScope.launch {
            _authState.value = AuthUiState.Loading
            val result = authUseCases.signInWithGoogle(activity, serverClientId)
            _authState.value = result.fold(
                onSuccess = {
                    getCurrentUser()
                    _authState.value // stays loading until getCurrentUser resolves
                },
                onFailure = { exception ->
                    AuthUiState.Error(mapExceptionToMessage(exception))
                }
            )
        }
    }

    fun checkAuthStatus() {
        viewModelScope.launch {
            _authState.value = AuthUiState.Loading
            val isAuthenticated = authUseCases.isAuthenticated()
            if (isAuthenticated) {
                getCurrentUser()
            } else {
                _authState.value = AuthUiState.Idle
            }
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            authUseCases.getCurrentUser().fold(
                onSuccess = { user ->
                    _authState.value = AuthUiState.Success(user)
                },
                onFailure = { exception ->
                    _authState.value = AuthUiState.Error(mapExceptionToMessage(exception))
                }
            )
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _authState.value = AuthUiState.Loading
            authUseCases.signOut().fold(
                onSuccess = {
                    _authState.value = AuthUiState.Idle
                },
                onFailure = { exception ->
                    _authState.value = AuthUiState.Error(mapExceptionToMessage(exception))
                }
            )
        }
    }

    fun getIdToken(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _authState.value = AuthUiState.Loading
            authUseCases.getIdToken(forceRefresh).fold(
                onSuccess = { token ->
                    val currentUser = (authState.value as? AuthUiState.Success)?.user
                    _authState.value = AuthUiState.Success(currentUser)
                },
                onFailure = { exception ->
                    _authState.value = AuthUiState.Error(mapExceptionToMessage(exception))
                }
            )
        }
    }

    private fun mapExceptionToMessage(exception: Throwable): String {
        return when (exception) {
            is GetCredentialException -> "Failed to retrieve Google credentials: ${exception.message}"
            is GoogleIdTokenParsingException -> "Invalid Google ID token: ${exception.message}"
            else -> exception.message ?: "An unexpected error occurred"
        }
    }
}

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: UserModel? = null) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}