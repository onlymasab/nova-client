package com.paandaaa.nova.android.viewmodel

import androidx.lifecycle.ViewModel
import com.paandaaa.nova.android.domain.model.OnboardingModel
import com.paandaaa.nova.android.domain.usecase.GetOnboardingPagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject



@HiltViewModel
class OnboardingViewModel @Inject constructor(
    getPagesUseCase: GetOnboardingPagesUseCase
) : ViewModel() {

    // Pages fetched once from the use case
    private val _pages: List<OnboardingModel> = getPagesUseCase()
    val pages: List<OnboardingModel> get() = _pages // Expose if needed

    val totalPages: Int get() = _pages.size

    // State for the current page index
    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    // Get current page's data
    fun getCurrentPage(): OnboardingModel = _pages[_currentPage.value]

    // Navigate to next page
    fun nextPage() {
        if (_currentPage.value < totalPages - 1) {
            _currentPage.value++
        }
    }

    // Navigate to previous page
    fun prevPage() {
        if (_currentPage.value > 0) {
            _currentPage.value--
        }
    }

    // Skip to the last page
    fun skip() {
        _currentPage.value = totalPages - 1
    }

    // Check if current page is the last
    fun isLastPage(): Boolean = _currentPage.value == totalPages - 1
}