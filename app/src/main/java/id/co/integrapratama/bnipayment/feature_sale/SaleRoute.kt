package id.co.integrapratama.bnipayment.feature_sale

import kotlinx.serialization.Serializable

sealed interface SaleRoute {
    @Serializable
    data object InputAmount : SaleRoute

    @Serializable
    data object InsertCard : SaleRoute

    @Serializable
    data object ConfirmCard : SaleRoute

    @Serializable
    data object TransactionStatus : SaleRoute
}
