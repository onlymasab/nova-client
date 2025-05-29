package com.paandaaa.nova.android.domain.usecase.auth

import com.paandaaa.nova.android.domain.repository.AuthRepository
import javax.inject.Inject

class IsAuthenticatedUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        return authRepository.isAuthenticated()
    }
}