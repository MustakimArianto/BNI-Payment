package id.co.integrapratama.sdk.core.utils

import id.co.integrapratama.sdk.feature_bin_range.domain.CardClassification

fun CardClassification.toText(): String {
    return when (this) {
        CardClassification.DEBIT -> "Debit"
        CardClassification.CREDIT -> "Credit"
        else -> "Unknown"
    }
}

fun String.toCardClassification(): CardClassification {
    return when (this.lowercase()) {
        "debit" -> CardClassification.DEBIT
        "credit" -> CardClassification.CREDIT
        else -> CardClassification.UNKNOWN
    }
}