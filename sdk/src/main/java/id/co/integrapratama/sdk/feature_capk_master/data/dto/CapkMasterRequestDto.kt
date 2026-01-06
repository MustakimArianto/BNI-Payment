package id.co.integrapratama.sdk.feature_capk_master.data.dto

import com.google.gson.annotations.SerializedName


data class CapkMasterRequestDto(
    @SerializedName("req")
    val requestAidMaster: RequestCapkMaster
)

data class RequestCapkMaster(
    val sn: String
)