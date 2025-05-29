package com.paandaaa.nova.android.domain.usecase.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.paandaaa.nova.android.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository // ✅ Injected for token auth
) {
    suspend operator fun invoke(serverClientId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val credentialManager = CredentialManager.create(context)

            // Step 1: Generate raw nonce and hash it
            val rawNonce = UUID.randomUUID().toString()
            val hashedNonce = hashNonce(rawNonce)

            // Step 2: Build Google ID option
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(serverClientId)
                .setNonce(hashedNonce)
                .build()

            // Step 3: Get credentials
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(context, request)
            val credential = GoogleIdTokenCredential.createFrom(result.credential.data)
            val idToken = credential.idToken

            // Step 4: Pass token and rawNonce to AuthRepository (e.g., Supabase)
            authRepository.signInWithGoogle(idToken, rawNonce)

            Result.success(Unit)

        } catch (e: GoogleIdTokenParsingException) {
            Log.e("GoogleSignIn", "Token parsing failed", e)
            Result.failure(e)
        } catch (e: GetCredentialException) {
            Log.e("GoogleSignIn", "Credential error", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "Unexpected error", e)
            Result.failure(e)
        }
    }

    private fun hashNonce(nonce: String): String {
        return MessageDigest
            .getInstance("SHA-256")
            .digest(nonce.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}