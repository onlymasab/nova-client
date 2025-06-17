package com.paandaaa.nova.android.data.repository

import com.paandaaa.nova.android.domain.model.UserModel
import com.paandaaa.nova.android.domain.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val client: SupabaseClient // Injected via Hilt
) : AuthRepository {

    // Google sign-in
    @OptIn(SupabaseExperimental::class)
    override suspend fun signInWithGoogle(idToken: String, rawNonce: String): Result<Unit> {
        return try {
            client.auth.signInWith(IDToken) {
                this.idToken = idToken          // Google-issued ID token
                this.provider = Google          // Provider is Google
                this.nonce = rawNonce           // Raw nonce, must match nonce used during Google request
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e) // Return the exception in case of failure
        }
    }

    // Check authentication state
    override suspend fun isAuthenticated(): Boolean {
        return try {
            val session = client.auth.currentSessionOrNull()
            session != null // Check if session is valid
        } catch (e: Exception) {
            false
        }
    }

    // Get current user information
    override suspend fun getCurrentUser(): Result<UserModel?> {
        return try {
            val session = client.auth.currentSessionOrNull()
            val userInfo = session?.user
            val userModel = userInfo?.let {
                UserModel(
                    id = it.id,
                    email = it.email ?: "",
                    name = it.userMetadata?.get("name")?.toString() ?: "",
                    profilePictureUrl = it.userMetadata?.get("picture")?.toString() ?: "",
                    // Add other fields as needed based on UserModel definition
                )
            }
            Result.success(userModel)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Sign out
    override suspend fun signOut(): Result<Unit> {
        return try {
            client.auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get ID token
    override suspend fun getIdToken(forceRefresh: Boolean): Result<String?> {
        return try {
            val session = if (forceRefresh) {
                client.auth.refreshCurrentSession()
                client.auth.currentSessionOrNull()
            } else {
                client.auth.currentSessionOrNull()
            }
            val token = session?.accessToken
            Result.success(token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}