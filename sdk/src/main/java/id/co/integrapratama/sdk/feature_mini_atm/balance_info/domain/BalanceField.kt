package id.co.integrapratama.sdk.feature_mini_atm.balance_info.domain

data class BalanceField(
    val fieldLength: Int,
    val usageIndicator: Char,
    val ledgerBalance: Long,
    val availableBalance: Long
)

fun parseBalanceField(raw: String): BalanceField {
    require(raw.length >= 27) { "Invalid field length" }

    val fieldLength = raw.take(2).toInt()
    val usageIndicator = raw[2]

    val ledgerBalance = raw
        .substring(3, 15).dropLast(2)
        .toLong()

    val availableBalance = raw
        .substring(15, 27).dropLast(2)
        .toLong()

    return BalanceField(
        fieldLength = fieldLength,
        usageIndicator = usageIndicator,
        ledgerBalance = ledgerBalance,
        availableBalance = availableBalance
    )
}