package id.co.integrapratama.sdk.feature_read_card.domain

import id.co.integrapratama.sdk.core.model.CustomPinpadUiBounds
import id.co.payment2go.terminalsdkhelper.common.emv.CardOption
import id.co.payment2go.terminalsdkhelper.common.emv.OnInsertOfflinePinAction
import id.co.payment2go.terminalsdkhelper.common.emv.OnInsertOnlinePinAction
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import id.co.payment2go.terminalsdkhelper.ingenico.emv.VerifyEMVResult
import kotlinx.coroutines.flow.Flow

interface ReadCardRepository {
    suspend fun readCard(cardOption: CardOption): Flow<Resource<ReadCardModel>>
    suspend fun injectAids(): Flow<Resource<Unit>>
    suspend fun injectCapks(): Flow<Resource<Unit>>

    suspend fun confirmCard(amount: Long) : Flow<Resource<Unit>>

    suspend fun physicalPinpad(
        cardNumber: String,
        onInsertOnlinePinAction: OnInsertOnlinePinAction?
    ): Flow<PinPadEvent>

    suspend fun physicalOfflinePinpad(
        cardNumber: String,
        onInsertOfflinePinAction: OnInsertOfflinePinAction?
    ): Flow<OfflinePinPadEvent>

    suspend fun screenPinpad(
        cardNumber: String,
        containerInfo: CustomPinpadUiBounds,
        pinpadMap: List<CustomPinpadUiBounds>,
        onInsertOnlinePinAction: OnInsertOnlinePinAction?,
    ): Flow<PinPadEvent>

    suspend fun screenOfflinePinpad(
        cardNumber: String,
        containerInfo: CustomPinpadUiBounds,
        pinpadMap: List<CustomPinpadUiBounds>,
        onInsertOfflinePinAction: OnInsertOfflinePinAction?
    ): Flow<OfflinePinPadEvent>

    suspend fun confirmInputPin(pin: String, isNonePin: Boolean)

    suspend fun confirmOfflineInputPin(pin: String, isNonePin: Boolean)

    suspend fun verifyEMVHost(
        emvHost: String?,
        authCode: String?,
        arc: String? = null,
        authorizeFlag: String? = null,
    ): Flow<Resource<Unit>>
}