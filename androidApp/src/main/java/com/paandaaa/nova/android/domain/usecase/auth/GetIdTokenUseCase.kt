package com.paandaaa.nova.android.domain.usecase.auth

import com.paandaaa.nova.android.domain.repository.AuthRepository
import javax.inject.Inject

class GetIdTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(forceRefresh: Boolean = false): Result<String?> {
        return authRepository.getIdToken(forceRefresh)
    }
}