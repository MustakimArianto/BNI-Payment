package id.co.integrapratama.sdk.core.utils

import id.co.integrapratama.sdk.feature_bin_range.domain.BinType

fun BinType.toTransactionScopeText(): String {
    return if (isOnUs) "On Us" else "Off Us"
}