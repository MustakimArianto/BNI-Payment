package id.co.integrapratama.sdk.feature_card_list.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "card_list",
)
data class CardListEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val acquirerId: Int,
    val active: Int,
    val cardTypeId: Int,
    val createdBy: Int,
    val createdDate: String,
    val isOnUs: Int,
    val issuerId: Int,
    val level: Int,
    val maxValue: Long,
    val minValue: Long,
    val modifiedBy: Int?,
    val modifiedDate: String?,
    val name: String,
    val nii: Int
)

