package id.co.integrapratama.sdk.feature_settlement.domain

data class SettlementSummaryModel(
    val title: String,
    val subSummary: List<SettlementSubSummaryModel>
)