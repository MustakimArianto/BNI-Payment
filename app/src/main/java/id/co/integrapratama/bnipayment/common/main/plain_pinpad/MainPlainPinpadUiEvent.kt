package id.co.integrapratama.bnipayment.common.main.plain_pinpad

import id.co.integrapratama.bnipayment.common.ui_component.PinpadInputEvent

sealed class MainPlainPinpadUiEvent {
    data class OnPinpadInputBasedEvent(
        val event: PinpadInputEvent
    ): MainPlainPinpadUiEvent()

    data class UpdateInputPlainPinpadPhase(
        val inputPhase: InputPlainPinpadPhase
    ): MainPlainPinpadUiEvent()

    data class SetAbleToTap(
        val ableToInput: Boolean
    ): MainPlainPinpadUiEvent()

    data class SetTitle(
        val title: String
    ): MainPlainPinpadUiEvent()

    data class SetDescription(
        val description: String
    ): MainPlainPinpadUiEvent()

    data class SetPinpadCancelConfig(
        val pinpadCancelConfig: InputPlainPinpadCancelConfig?
    ): MainPlainPinpadUiEvent()
}