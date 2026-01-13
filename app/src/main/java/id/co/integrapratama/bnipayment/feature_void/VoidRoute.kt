package id.co.integrapratama.bnipayment.feature_void

import kotlinx.serialization.Serializable

sealed interface VoidRoute {
    @Serializable
    data object InputTraceNo : VoidRoute

    @Serializable
    data object ConfirmVoid : VoidRoute

    @Serializable
    data object TransactionStatus : VoidRoute
}
