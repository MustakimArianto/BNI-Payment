package id.co.integrapratama.sdk.feature_settlement.domain

data class TotalSettlementSummaryModel(
    val totalSale: Long,
    val totalVoid: Long,
    val totalRefund: Long
)