package id.co.integrapratama.sdk.feature_capk_master.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import id.co.payment2go.terminalsdkhelper.common.emv.CapkParam
import id.co.payment2go.terminalsdkhelper.landi.LandiBytesUtil

@Entity(
    tableName = "capk_master",
)
data class CapkMasterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val exp: String,
    val expDate: String,
    val index: String,
    val mod: String,
    val rid: String,
    val hashFlag: Byte,
    val hash: String,
    val algorithm: Int
)

fun CapkMasterEntity.toCapkParam(): CapkParam {
    var exp: ByteArray? = null
    var expDate: ByteArray? = null
    var index: Byte = 0
    var mod: ByteArray? = null
    var rid: ByteArray? = null
    var hashFlag: Byte = 0
    var hash: ByteArray? = null
    var algorithm: Int = 1
    if (this.exp.isNotEmpty()) {
        exp = LandiBytesUtil.hexString2Bytes(this.exp)
    }
    if (this.expDate.isNotEmpty()) {
        expDate = LandiBytesUtil.hexString2Bytes(this.expDate)
    }
    if (this.mod.isNotEmpty()) {
        mod = LandiBytesUtil.hexString2Bytes(this.mod)
    }
    if (this.index.isNotEmpty()) {
        val indexByteArray = LandiBytesUtil.hexString2Bytes(this.index)
        if (indexByteArray.isNotEmpty()) {
            index = indexByteArray.first()
        }
    }
    if (this.rid.isNotEmpty()) {
        rid = LandiBytesUtil.hexString2Bytes(this.rid)
    }
    hashFlag = this.hashFlag
    if (this.hash.isNotEmpty()) {
        hash = LandiBytesUtil.hexString2Bytes(this.hash)
    }
    algorithm = this.algorithm
    return CapkParam(
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