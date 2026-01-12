package id.co.integrapratama.sdk.feature_card_list.data.remote

import id.co.integrapratama.sdk.feature_card_list.data.dto.CardListRequestDto
import id.co.integrapratama.sdk.feature_card_list.data.dto.CardListResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CardListApi {
    @POST("getCardListBni")
    suspend fun getCardList(@Body request: CardListRequestDto): Response<CardListResponseDto>
}