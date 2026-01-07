package id.co.integrapratama.sdk.feature_read_card.domain

import id.co.payment2go.terminalsdkhelper.common.emv.OnInsertOnlinePinAction
import id.co.payment2go.terminalsdkhelper.common.pinpad.OnPinPadResult
import id.co.payment2go.terminalsdkhelper.landi.pinpad.CustomPinPadState

data class PinPadEvent(
    val result: OnPinPadResult? = null,
    val action: OnInsertOnlinePinAction? = null,
    val pinpadState: CustomPinPadState? = null,  // Add this
)