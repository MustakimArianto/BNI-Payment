package id.co.integrapratama.sdk.feature_aid_master.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "aid_master",
)
data class AidMasterEntity(
    @PrimaryKey
    val aids: String
)