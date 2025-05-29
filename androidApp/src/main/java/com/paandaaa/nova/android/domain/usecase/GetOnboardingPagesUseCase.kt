package com.paandaaa.nova.android.domain.usecase

import com.paandaaa.nova.android.domain.repository.OnboardingRepository
import javax.inject.Inject

class GetOnboardingPagesUseCase @Inject constructor(
    private val repository: OnboardingRepository
) {
    operator fun invoke() = repository.getOnboardingPages()


}

