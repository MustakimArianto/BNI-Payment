package id.co.integrapratama.bnipayment.feature_onboarding

import kotlinx.serialization.Serializable

sealed interface OnboardingRoute {
    @Serializable
    data object Splash : OnboardingRoute

    @Serializable
    data object Welcome : OnboardingRoute
}