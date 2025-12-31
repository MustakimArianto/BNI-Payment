package id.co.integrapratama.bnipayment.feature_admin

import kotlinx.serialization.Serializable

sealed interface AdminRoute {
    @Serializable
    data object Menu : AdminRoute
}