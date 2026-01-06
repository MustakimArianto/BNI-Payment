package id.co.integrapratama.sdk.feature_read_card.domain

import id.co.payment2go.terminalsdkhelper.common.emv.OnInsertOnlinePinAction
import id.co.payment2go.terminalsdkhelper.core.util.CardReadOutput

data class ReadCardModel(
    val cardReadOutput: CardReadOutput = CardReadOutput(),
    val isShowPinpad :Boolean = false,
    val onInsertOnlinePinAction: OnInsertOnlinePinAction? = null
)