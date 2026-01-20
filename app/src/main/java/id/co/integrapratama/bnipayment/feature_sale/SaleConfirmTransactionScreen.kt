package id.co.integrapratama.bnipayment.feature_sale

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.bnipayment.common.ui_component.CardNumberCard
import id.co.integrapratama.bnipayment.common.ui_component.CustomPinpad
import id.co.integrapratama.bnipayment.common.ui_component.PrimaryButton
import id.co.integrapratama.bnipayment.common.ui_component.TopBar
import id.co.integrapratama.bnipayment.common.ui_component.spacer.SpacerSize
import id.co.integrapratama.bnipayment.common.ui_component.spacer.VerticalSpacer
import id.co.integrapratama.bnipayment.ui.theme.LightGray
import id.co.integrapratama.bnipayment.ui.theme.TextGrayColor
import id.co.integrapratama.sdk.core.model.AIDName
import id.co.integrapratama.sdk.core.model.CustomPinpadUiBounds
import id.co.integrapratama.sdk.core.utils.StringUtil

@Composable
fun SaleConfirmTransactionScreen(
    title: String,
    amount: String,
    tip: String,
    total: String,
    pin: String,
    aidName: AIDName,
    cardNumber: String,
    isPhysicalKeyboard: Boolean,
    showPinpad: Boolean = false,
    showOfflinePinpad: Boolean = false,
    onNext: () -> Unit,
    onButtonMapReady: (CustomPinpadUiBounds, List<CustomPinpadUiBounds>) -> Unit,
    onOfflinePinButtonMapReady: (CustomPinpadUiBounds, List<CustomPinpadUiBounds>) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopBar(title = title)

        Column(Modifier.padding(16.dp)) {
            Text(
                stringResource(R.string.message_confirm_transaction),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            VerticalSpacer(SpacerSize.MEDIUM)
            CardNumberCard(cardNumber, aidName)
            VerticalSpacer(SpacerSize.X_LARGE)
            AmountAndTipSection(amount, tip)
            Text(stringResource(R.string.text_total_amount))
            VerticalSpacer(SpacerSize.MEDIUM)
            Text(
                StringUtil.formatRupiahCurrency(total),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        }
    }


    Box(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Column(Modifier.fillMaxWidth()) {
                HorizontalDivider(color = LightGray)
                PrimaryButton(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(R.string.button_next),
                    onClick = onNext
                )
            }
        }
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

@Composable
fun AmountAndTipSection(amount: String, tip: String) {
    Column(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(stringResource(R.string.text_amount), color = TextGrayColor)
            Text(text = StringUtil.formatRupiahCurrency(amount))
        }
        VerticalSpacer(SpacerSize.MEDIUM)
        HorizontalDivider(thickness = 1.dp, color = LightGray)
        VerticalSpacer(SpacerSize.LARGE)

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(stringResource(R.string.text_tip), color = TextGrayColor)
            Text(text = StringUtil.formatRupiahCurrency(tip))
        }
        VerticalSpacer(SpacerSize.MEDIUM)
        HorizontalDivider(thickness = 1.dp, color = LightGray)
        VerticalSpacer(SpacerSize.LARGE)
    }
}