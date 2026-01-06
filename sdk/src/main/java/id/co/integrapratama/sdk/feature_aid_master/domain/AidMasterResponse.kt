package id.co.integrapratama.sdk.feature_aid_master.domain

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import id.co.integrapratama.sdk.feature_aid_master.data.dto.DataUpdateAid

@Keep
data class AidMasterResponse(
    @SerializedName("data")
    val dataUpdateAid: DataUpdateAid,
    val initTime: String,
    val responseCode: String,
    val responseDesc: String,
    val rows: List<String>
)
