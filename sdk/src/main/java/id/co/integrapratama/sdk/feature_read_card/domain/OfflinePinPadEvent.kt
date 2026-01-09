package id.co.integrapratama.sdk.feature_read_card.domain

import id.co.payment2go.terminalsdkhelper.common.emv.OnInsertOfflinePinAction
import id.co.payment2go.terminalsdkhelper.common.pinpad.OnPinPadResult
import id.co.payment2go.terminalsdkhelper.landi.pinpad.CustomPinPadState

data class OfflinePinPadEvent(
    val result: OnPinPadResult? = null,
    val action: OnInsertOfflinePinAction? = null,
    val pinpadState: CustomPinPadState? = null,  // Add this
)