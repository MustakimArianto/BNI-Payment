package id.co.integrapratama.sdk.feature_void.domain

import androidx.annotation.Keep
import id.co.integrapratama.sdk.core.model.AIDName

@Keep
data class ListVoidModel(
    val cardNo: String,
    val invoice: String,
    val invoiceDate: String,
    val aidName: AIDName,
    val refNo: String,
    val amount: Long,
    val tip: Long,
    val customerName: String,
    val cardClassification: String,
    val transactionScope: String
)