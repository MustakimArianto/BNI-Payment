package id.co.integrapratama.sdk.feature_card_list.data.dto

import androidx.annotation.Keep
import id.co.integrapratama.sdk.feature_card_list.data.local.CardListEntity

@Keep
data class CardListDto(
    val acquirerId: Int,
    val active: Int,
    val cardTypeId: Int,
    val createdBy: Int,
    val createdDate: String,
    val id: Int,
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

fun CardListDto.toEntity(): CardListEntity {
    return CardListEntity(
        acquirerId = acquirerId,
        active = active,
        cardTypeId = cardTypeId,
        createdBy = createdBy,
        createdDate = createdDate,
        isOnUs = isOnUs,
        issuerId = issuerId,
        level = level,
        maxValue = maxValue,
        minValue = minValue,
        modifiedBy = modifiedBy,
        modifiedDate = modifiedDate,
        name = name,
        nii = nii
    )
}