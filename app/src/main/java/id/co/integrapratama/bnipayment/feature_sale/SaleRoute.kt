package id.co.integrapratama.bnipayment.feature_sale

import kotlinx.serialization.Serializable

sealed interface SaleRoute {
    @Serializable
    data object InputAmount : SaleRoute
}