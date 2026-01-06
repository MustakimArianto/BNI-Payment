package id.co.integrapratama.sdk.feature_aid_master.data.dto

import com.google.gson.annotations.SerializedName
import id.co.integrapratama.sdk.feature_aid_master.data.local.AidMasterEntity
import id.co.integrapratama.sdk.feature_aid_master.domain.AidMasterResponse

data class AidMasterResponseDto(
    @SerializedName("data")
    val dataUpdateAid: DataUpdateAid,
    val initTime: String,
    val responseCode: String,
    val responseDesc: String,
    val rows: List<String>
)

fun AidMasterResponseDto.toEntity(): List<AidMasterEntity> {
    return rows.map { aidString ->
        AidMasterEntity(
            aids = aidString
        )
    }
}

fun AidMasterResponseDto.toModel() : AidMasterResponse {
    return AidMasterResponse(
        dataUpdateAid = dataUpdateAid,
        initTime = initTime,
        responseCode = responseCode,
        responseDesc = responseDesc,
        rows = rows
    )
}