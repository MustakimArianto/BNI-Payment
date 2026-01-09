package id.co.integrapratama.bnipayment.feature_installment

import kotlinx.serialization.Serializable

sealed interface InstallmentRoute {
    @Serializable
    data object InputAmount : InstallmentRoute

    @Serializable
    data object InsertCard : InstallmentRoute

    @Serializable
    data object ConfirmTransaction : InstallmentRoute

    @Serializable
    data object TransactionStatus : InstallmentRoute
}
