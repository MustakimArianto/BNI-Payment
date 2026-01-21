package id.co.integrapratama.bnipayment.feature_settlement

sealed class SettlementUiEvent {
    data object LoadTotalSettlement : SettlementUiEvent()
    data object ShowPerformSettlementAndBatchUploadPromptDialog : SettlementUiEvent()
    data class ApplyPerformSettlementAndBatchUploadPromptDialog(
        val type: ApplyPerformSettlementAndBatchUploadPromptDialogType
    ) : SettlementUiEvent()
    data object ClearErrorMessage : SettlementUiEvent()
}