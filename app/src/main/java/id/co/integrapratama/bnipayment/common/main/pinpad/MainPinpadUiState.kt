package id.co.integrapratama.bnipayment.common.main.pinpad

import id.co.payment2go.terminalsdkhelper.common.emv.OnInsertOfflinePinAction
import id.co.payment2go.terminalsdkhelper.common.emv.OnInsertOnlinePinAction

data class MainPinpadUiState(
    val onInsertOnlinePinAction: OnInsertOnlinePinAction? = null,
    val onInsertOfflinePinAction: OnInsertOfflinePinAction? = null
)