package id.co.integrapratama.bnipayment.common.main.plain_pinpad

sealed class InputPlainPinpadPhase(
    val phaseCode: Int
) {
    object InputPhase : InputPlainPinpadPhase(1)
    object PreCheckingPhase : InputPlainPinpadPhase(2)
    object CheckingPhase : InputPlainPinpadPhase(3)
    object AcceptPhase : InputPlainPinpadPhase(4)
    object DeclinePhase : InputPlainPinpadPhase(5)
    data class CancelPhase(
        val withDisableTap: Boolean = false
    ) : InputPlainPinpadPhase(6)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InputPlainPinpadPhase

        return phaseCode == other.phaseCode
    }

    override fun hashCode(): Int {
        return phaseCode
    }
}