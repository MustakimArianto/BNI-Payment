package id.co.integrapratama.bnipayment.common.main.plain_pinpad

import androidx.lifecycle.ViewModel
import id.co.integrapratama.bnipayment.common.ui_component.PinpadInputEvent
import id.co.integrapratama.bnipayment.common.ui_component.PinpadInputEventCancelPinBehavior
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainPlainPinpadViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MainPlainPinpadUiState())
    val uiState: StateFlow<MainPlainPinpadUiState> = _uiState.asStateFlow()

    fun onEvent(event: MainPlainPinpadUiEvent) {
        when (event) {
            is MainPlainPinpadUiEvent.OnPinpadInputBasedEvent -> {
                val pinpadInputEvent = event.event
                when (pinpadInputEvent) {
                    is PinpadInputEvent.InputPin -> {
                        _uiState.update {
                            it.copy(
                                pin = pinpadInputEvent.pin
                            )
                        }
                    }
                    is PinpadInputEvent.CancelPin -> {
                        val cancelPinBehavior = pinpadInputEvent.behavior
                        when (cancelPinBehavior) {
                            is PinpadInputEventCancelPinBehavior.FirstCancelPinBehavior -> {
                                _uiState.update {
                                    it.copy(
                                        pin = pinpadInputEvent.behavior.pin
                                    )
                                }
                            }
                            is PinpadInputEventCancelPinBehavior.SecondCancelPinBehavior -> {
                                val pinpadCancelConfig = _uiState.value.pinpadCancelConfig
                                _uiState.update {
                                    it.copy(
                                        inputPhase = InputPlainPinpadPhase.CancelPhase(
                                            pinpadCancelConfig?.withDisableTap ?: true
                                        )
                                    )
                                }
                            }
                        }
                    }
                    is PinpadInputEvent.ClearPin -> {
                        _uiState.update {
                            it.copy(
                                pin = pinpadInputEvent.pin
                            )
                        }
                    }
                    is PinpadInputEvent.EnterPin -> {
                        _uiState.update {
                            it.copy(
                                pin = pinpadInputEvent.pin,
                                ableToTap = false,
                                inputPhase = InputPlainPinpadPhase.PreCheckingPhase
                            )
                        }
                    }
                }
            }
            is MainPlainPinpadUiEvent.UpdateInputPlainPinpadPhase -> {
                _uiState.update {
                    it.copy(
                        inputPhase = event.inputPhase
                    )
                }
            }
            is MainPlainPinpadUiEvent.SetAbleToTap -> {
                _uiState.update {
                    it.copy(
                        ableToTap = event.ableToInput
                    )
                }
            }
            is MainPlainPinpadUiEvent.SetTitle -> {
                _uiState.update {
                    it.copy(
                        title = event.title
                    )
                }
            }
            is MainPlainPinpadUiEvent.SetDescription -> {
                _uiState.update {
                    it.copy(
                        description = event.description
                    )
                }
            }
            is MainPlainPinpadUiEvent.SetPinpadCancelConfig -> {
                _uiState.update {
                    it.copy(
                        pinpadCancelConfig = event.pinpadCancelConfig
                    )
                }
            }
        }
    }
}