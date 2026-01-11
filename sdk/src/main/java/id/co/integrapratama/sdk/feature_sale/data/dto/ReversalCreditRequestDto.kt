package id.co.integrapratama.sdk.feature_sale.data.dto

import androidx.annotation.Keep
import com.google.gson.Gson

@Keep
data class ReversalCreditRequestDto(
    val pan: String,
    val processingCode: String,
    val amount: String,
    val stan: String,
    val time: String,
    val date: String,
    val expiry: String,
    val posEntryMode: String,
    val panSeq: String,
    val nii: String,
    val posConditionCode: String,
    val approvalCode: String,
    val responseCode: String,
    val tid: String,
    val mid: String,
    val iccData: String,
    val fld57: String,
    val transactionDetails: String,
    val fld62: String,
    val messageAuthCode: String,
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }

    companion object {
        fun fromString(jsonString: String): ReversalCreditRequestDto {
            return Gson().fromJson(jsonString, ReversalCreditRequestDto::class.java)
        }
    }
}


