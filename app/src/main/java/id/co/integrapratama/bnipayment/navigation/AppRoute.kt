package id.co.integrapratama.bnipayment.navigation

import kotlinx.serialization.Serializable

sealed interface AppRoute {
    @Serializable
    data object Onboarding : AppRoute

    @Serializable
    data object Main : AppRoute

    @Serializable
    data object Home : AppRoute

    @Serializable
    data object MiniATM : AppRoute

    @Serializable
    data class Sale(val isContactless: Boolean) : AppRoute

    @Serializable
    data object Void : AppRoute

    @Serializable
    data object Installment : AppRoute

    @Serializable
    data object Settlement : AppRoute

    @Serializable
    data object Information : AppRoute

    @Serializable
    data object Admin : AppRoute

    @Serializable
    data object Account : AppRoute
}