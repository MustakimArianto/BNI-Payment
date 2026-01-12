package id.co.integrapratama.sdk.feature_sale.data.dto

import com.google.gson.Gson

data class ReversalDebitRequestDto(
    val pan: String,
    val processingCode: String,
    val amount: String,
    val transactionDateTime: String,
    val stan: String,
    val time: String,
    val date: String,
    val expiry: String,
    val posEntryMode: String,
    val nii: String,
    val tid: String,
    val mid: String,
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }

    companion object {
        fun fromString(jsonString: String): ReversalDebitRequestDto {
            return Gson().fromJson(jsonString, ReversalDebitRequestDto::class.java)
        }
    }
}