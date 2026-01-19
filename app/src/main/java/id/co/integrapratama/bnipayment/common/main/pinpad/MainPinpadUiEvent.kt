package id.co.integrapratama.bnipayment.common.main.pinpad

import id.co.payment2go.terminalsdkhelper.common.emv.OnInsertOfflinePinAction
import id.co.payment2go.terminalsdkhelper.common.emv.OnInsertOnlinePinAction

sealed class MainPinpadUiEvent {
    data class ShowPinpad(
        val onInsertOnlinePinAction: OnInsertOnlinePinAction
    ): MainPinpadUiEvent()

    data class ShowOfflinePinpad(
        val onInsertOfflinePinAction: OnInsertOfflinePinAction
    ): MainPinpadUiEvent()
}