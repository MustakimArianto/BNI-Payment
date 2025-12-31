package id.co.integrapratama.bnipayment.feature_home

import kotlinx.serialization.Serializable

sealed interface HomeRoute {
    @Serializable
    data object Menu : HomeRoute
}