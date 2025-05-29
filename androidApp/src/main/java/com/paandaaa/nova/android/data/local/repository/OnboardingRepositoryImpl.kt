package com.paandaaa.nova.android.data.local.repository

import com.paandaaa.nova.android.domain.model.OnboardingModel
import com.paandaaa.nova.android.domain.repository.OnboardingRepository
import com.paandaaa.nova.android.R

class OnboardingRepositoryImpl : OnboardingRepository {
    override fun getOnboardingPages(): List<OnboardingModel> {
        return listOf(
            OnboardingModel(
                title = "Welcome to Nova",
                description ="Your Intelligent Virtual Assistant.\nThinking. Learning. Evolving.",
                imageRes = R.drawable.onboarding_cover_1
            ),
            OnboardingModel(
                title = "I Learn, I Adapt,\nI Understand",
                description = "Let’s Explore the Future Together.",
                imageRes = R.drawable.onboarding_cover_2
            ),
            OnboardingModel(
                title = "Ready to Begin?",
                description = "",
                imageRes = R.drawable.onboarding_cover_3
            )
        )
    }
}