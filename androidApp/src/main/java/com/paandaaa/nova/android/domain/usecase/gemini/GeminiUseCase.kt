package com.paandaaa.nova.android.domain.usecase.gemini

import com.paandaaa.nova.android.domain.usecase.auth.SignInWithGoogleUseCase
import javax.inject.Inject

data class GeminiUseCases @Inject constructor(
    val getGeminiResponseUseCase: GetGeminiResponseUseCase,
)