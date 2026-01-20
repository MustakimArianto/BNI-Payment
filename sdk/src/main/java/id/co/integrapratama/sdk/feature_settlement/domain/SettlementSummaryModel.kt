package id.co.integrapratama.sdk.feature_settlement.domain

data class SettlementSummaryModel(
    val title: String,
    val subSummaryList: List<SettlementSubSummaryModel>
)