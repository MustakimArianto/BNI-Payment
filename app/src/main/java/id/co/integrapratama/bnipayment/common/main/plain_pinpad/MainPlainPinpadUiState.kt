package id.co.integrapratama.bnipayment.common.main.plain_pinpad

data class MainPlainPinpadUiState(
    val title: String = "",
    val description: String = "",
    val pin: String = "",
    val pinMaxLength: Int = 4,
    val pinpadCancelConfig: InputPlainPinpadCancelConfig? = null,
    val ableToTap: Boolean = false,
    val isPhysicalKeyboard: Boolean = false,
    val inputPhase: InputPlainPinpadPhase = InputPlainPinpadPhase.InputPhase
)