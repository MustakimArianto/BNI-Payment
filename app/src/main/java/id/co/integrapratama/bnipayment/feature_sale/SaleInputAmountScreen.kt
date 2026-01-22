package id.co.integrapratama.bnipayment.feature_sale

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.co.integrapratama.bnipayment.common.ui_component.CustomAmountKeypad
import id.co.integrapratama.bnipayment.common.ui_component.CustomPinpad
import id.co.integrapratama.bnipayment.common.ui_component.InputAmountTextField
import id.co.integrapratama.bnipayment.common.ui_component.PrimaryToggle
import id.co.integrapratama.bnipayment.common.ui_component.SmallText
import id.co.integrapratama.bnipayment.common.ui_component.TopBar
import id.co.integrapratama.bnipayment.common.ui_component.spacer.SpacerSize
import id.co.integrapratama.bnipayment.common.ui_component.spacer.VerticalSpacer
import id.co.integrapratama.sdk.core.model.CustomPinpadUiBounds

enum class FocusedField {
    AMOUNT, TIP, NONE
}

@Composable
fun InputAmountScreen(
    title: String,
    amount: String,
    tip: String,
    pin: String,
    showPinpad: Boolean,
    showOfflinePinpad: Boolean,
    isPhysicalKeyboard: Boolean,
    onAmountChanged: (String) -> Unit,
    onTipChanged: (String) -> Unit,
    onOkClick: () -> Unit,
    onButtonMapReady: (CustomPinpadUiBounds, List<CustomPinpadUiBounds>) -> Unit,
    onOfflinePinButtonMapReady: (CustomPinpadUiBounds, List<CustomPinpadUiBounds>) -> Unit,
) {
    var isTipEnabled by remember { mutableStateOf(false) }
    var focusedField by remember { mutableStateOf(FocusedField.AMOUNT) }

    Column(
        Modifier.fillMaxSize()
    ) {
        TopBar(title = title)
        VerticalSpacer(SpacerSize.X_LARGE)
        Column(Modifier.padding(horizontal = 16.dp)) {
            SmallText(text = "Input Amount")
            InputAmountTextField(
                amount = amount,
                onAmountChanged = onAmountChanged,
                onFocusChanged = { isFocused ->
                    if (isFocused) {
                        focusedField = FocusedField.AMOUNT
                    }
                }
            )

            VerticalSpacer(SpacerSize.MEDIUM)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SmallText(text = "Input Tip")

                PrimaryToggle(
                    checked = isTipEnabled,
                    onCheckedChange = { isChecked ->
                        isTipEnabled = isChecked
                        if (!isChecked) {
                            onTipChanged("")
                            focusedField = FocusedField.AMOUNT
                        } else {
                            focusedField = FocusedField.TIP
                        }
                    }
                )
            }

            if (isTipEnabled) {
                VerticalSpacer(SpacerSize.MEDIUM)
                InputAmountTextField(
                    amount = tip,
                    onAmountChanged = onTipChanged,
                    onFocusChanged = { isFocused ->
                        if (isFocused) {
                            focusedField = FocusedField.TIP
                        }
                    }
                )
            }
        }

        Spacer(Modifier.weight(1f))

        CustomAmountKeypad(
            onNumberClick = { number ->
                when (focusedField) {
                    FocusedField.AMOUNT -> {
                        val newAmount = amount + number
                        if (amount.isEmpty() && number.all { it == '0' }) {
                            return@CustomAmountKeypad
                        }

                        if (newAmount.length <= 12) {
                            onAmountChanged(newAmount)
                        }
                    }

                    FocusedField.TIP -> {
                        val newTip = tip + number
                        if (tip.isEmpty() && number.all { it == '0' }) {
                            return@CustomAmountKeypad
                        }
                        if (newTip.length <= 12) {
                            onTipChanged(newTip)
                        }
                    }
                    FocusedField.NONE -> {}
                }
            },
            onBackspaceClick = {
                when (focusedField) {
                    FocusedField.AMOUNT -> {
                        if (amount.isNotEmpty()) {
                            onAmountChanged(amount.dropLast(1))
                        }
                    }
                    FocusedField.TIP -> {
                        if (tip.isNotEmpty()) {
                            onTipChanged(tip.dropLast(1))
                        }
                    }
                    FocusedField.NONE -> {}
                }
            },
            onClearClick = {
                when (focusedField) {
                    FocusedField.AMOUNT -> onAmountChanged("")
                    FocusedField.TIP -> onTipChanged("")
                    FocusedField.NONE -> {}
                }
            },
            onConfirmClick = onOkClick
        )
    }

    if (showPinpad) {
        CustomPinpad(
            pin = pin,
            isPhysicalKeyboard = isPhysicalKeyboard,
            onUpdatePinpadMapping = onButtonMapReady
        )
    } else if (showOfflinePinpad) {
        CustomPinpad(
            pin = pin,
            isPhysicalKeyboard = isPhysicalKeyboard,
            onUpdatePinpadMapping = onOfflinePinButtonMapReady
        )
    }
}