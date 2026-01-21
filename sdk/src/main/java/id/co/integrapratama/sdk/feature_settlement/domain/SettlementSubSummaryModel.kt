package id.co.integrapratama.sdk.feature_settlement.domain

data class SettlementSubSummaryModel(
    val title: String,
    val count: Int? = null,
    val amount: Long
)