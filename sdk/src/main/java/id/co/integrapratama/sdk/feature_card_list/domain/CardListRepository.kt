package id.co.integrapratama.sdk.feature_card_list.domain

import id.co.integrapratama.sdk.feature_card_list.data.dto.CardListRequestDto
import id.co.integrapratama.sdk.feature_card_list.data.dto.CardListResponseDto
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface CardListRepository {
    fun getCardList(): Flow<Resource<CardListResponseDto>>
}