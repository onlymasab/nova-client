package com.paandaaa.nova.android.domain.repository

import com.paandaaa.nova.android.domain.model.OnboardingModel

interface OnboardingRepository {
    fun getOnboardingPages(): List<OnboardingModel>
}