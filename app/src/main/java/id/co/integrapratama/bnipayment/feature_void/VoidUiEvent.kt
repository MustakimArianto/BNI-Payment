package id.co.integrapratama.bnipayment.feature_void

sealed class VoidUiEvent {
    data class OnTraceNoChange(val traceNo: String) : VoidUiEvent()
    data object ConfirmVoid : VoidUiEvent()
    data object SubmitVoid : VoidUiEvent()
    data object PrintReceipt : VoidUiEvent()
    data class SetErrorMessage(val errorMessage: String) : VoidUiEvent()
}