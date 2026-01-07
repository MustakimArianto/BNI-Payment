package id.co.integrapratama.sdk.feature_card_list.data.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CardListResponseDto(
    @SerializedName("data")
    val `data`: List<CardListDto>
)