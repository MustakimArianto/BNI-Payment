package id.co.integrapratama.bnipayment.feature_account

import kotlinx.serialization.Serializable

sealed interface AccountRoute {
    @Serializable
    object Menu : AccountRoute
}