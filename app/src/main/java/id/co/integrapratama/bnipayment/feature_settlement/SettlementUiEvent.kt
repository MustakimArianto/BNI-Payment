package id.co.integrapratama.bnipayment.feature_settlement

sealed class SettlementUiEvent {
    data object LoadTotalSettlement : SettlementUiEvent()
    data object ShowSettlementConfirmationDialog : SettlementUiEvent()
    data object PerformSettlementAndBatchUpload : SettlementUiEvent()
}