package id.co.integrapratama.sdk.feature_aid_master.data.dto

import com.google.gson.annotations.SerializedName


data class AidMasterRequestDto(
    @SerializedName("req")
    val requestAidMaster: RequestAidMaster
)

data class RequestAidMaster(
    val sn: String
)