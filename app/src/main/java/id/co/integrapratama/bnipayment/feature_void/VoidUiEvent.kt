package id.co.integrapratama.bnipayment.feature_void

import id.co.integrapratama.sdk.feature_void.domain.ListVoidModel

sealed class VoidUiEvent {
    data class OnTraceNoChange(val traceNo: String) : VoidUiEvent()
    data object ConfirmVoid : VoidUiEvent()
    data class TransactionListClicked(val transaction: ListVoidModel) : VoidUiEvent()
    data object SubmitVoid : VoidUiEvent()
    data object PrintReceipt : VoidUiEvent()
    data class SetErrorMessage(val errorMessage: String) : VoidUiEvent()
}