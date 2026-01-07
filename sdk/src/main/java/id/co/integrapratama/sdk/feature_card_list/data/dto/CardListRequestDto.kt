package id.co.integrapratama.sdk.feature_card_list.data.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CardListRequestDto(
    @SerializedName("req")
    val requestCardList: RequestCardList
)

data class RequestCardList(
    val sn: String
)