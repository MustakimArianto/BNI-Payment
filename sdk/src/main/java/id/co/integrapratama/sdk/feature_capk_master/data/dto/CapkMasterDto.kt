package id.co.integrapratama.sdk.feature_capk_master.data.dto

import id.co.integrapratama.sdk.feature_capk_master.data.local.CapkMasterEntity

data class CapkMasterDto(
    val exp: String,
    val expDate: String,
    val index: String,
    val mod: String,
    val rid: String,
    val hashFlag: Byte,
    val hash: String,
    val algorithm: Int
)

fun CapkMasterDto.toEntity(): CapkMasterEntity {
    return CapkMasterEntity(
        exp = exp,
        expDate = expDate,
        index = index,
        mod = mod,
        rid = rid,
        hashFlag = hashFlag,
        hash = hash,
        algorithm = algorithm
    )
}