package id.co.integrapratama.sdk.feature_capk_master.data.dto

import com.google.gson.annotations.SerializedName
import id.co.integrapratama.sdk.feature_capk_master.data.local.CapkMasterEntity

data class CapkMasterResponseDto(
    @SerializedName("pubKeyList")
    val rows: List<CapkMasterDto>
)

fun CapkMasterResponseDto.toEntity(): List<CapkMasterEntity> {
    return rows.map { capkMasterDto ->
        capkMasterDto.toEntity()
    }
}