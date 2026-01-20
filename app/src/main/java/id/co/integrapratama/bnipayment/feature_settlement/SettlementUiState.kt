package id.co.integrapratama.bnipayment.feature_settlement

import id.co.integrapratama.sdk.feature_settlement.domain.TotalSettlementSummaryModel
import id.co.payment2go.terminalsdkhelper.core.util.Resource

data class SettlementUiState(
    val isLoading: Boolean = false,
    val loadingMessage: String = "",
    val totalSettlementSummaryModelResult: Resource<TotalSettlementSummaryModel>? = null,
    val showPerformSettlementAndBatchUploadPromptDialog: Boolean = false
)