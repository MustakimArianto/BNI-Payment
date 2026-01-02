package id.co.integrapratama.bnipayment.feature_sale

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.co.integrapratama.bnipayment.common.spacer.SpacerSize
import id.co.integrapratama.bnipayment.common.spacer.VerticalSpacer
import id.co.integrapratama.bnipayment.common.ui.InputAmountTextField
import id.co.integrapratama.bnipayment.common.ui_component.PrimaryButton
import id.co.integrapratama.bnipayment.common.ui_component.TopBar

@Composable
fun InputAmountScreen(
    amount: String = "",
    onAmountChanged: (String) -> Unit,
    onNextClick: () -> Unit,
    onNavigationBack: () -> Unit,
) {
    Column(Modifier
        .padding(16.dp)
        .navigationBarsPadding()) {
        TopBar(title = "Sale", onBackClick = onNavigationBack)
        VerticalSpacer(SpacerSize.X_LARGE)

        InputAmountTextField(
            amount = amount,
            onAmountChanged = onAmountChanged,
            label = "Nominal"
        )

        Box(Modifier.fillMaxSize()) {
            PrimaryButton(
                modifier = Modifier.align(Alignment.BottomCenter),
                text = "Lanjut",
                onClick = onNextClick
            )
        }
    }
}