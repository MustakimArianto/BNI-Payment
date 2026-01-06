package id.co.integrapratama.sdk.feature_read_card.domain

import id.co.payment2go.terminalsdkhelper.common.emv.CardOption
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface ReadCardRepository {
    suspend fun readCard(cardOption: CardOption): Flow<Resource<ReadCardModel>>
    suspend fun injectAids(): Flow<Resource<Unit>>
    suspend fun injectCapks(): Flow<Resource<Unit>>

    suspend fun confirmCard(amount: Long) : Flow<Resource<Unit>>
}