package id.co.integrapratama.bnipayment.feature_void

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.co.integrapratama.bnipayment.common.ui_component.spacer.SpacerSize
import id.co.integrapratama.bnipayment.common.ui_component.spacer.VerticalSpacer
import id.co.integrapratama.bnipayment.common.ui_component.PrimaryButton
import id.co.integrapratama.bnipayment.common.ui_component.PrimaryTextField
import id.co.integrapratama.bnipayment.common.ui_component.TopBar

@Composable
fun VoidInputTraceNoScreen(
    traceNo: String = "",
    onTraceNoChanged: (String) -> Unit,
    onNextClick: () -> Unit,
    onNavigationBack: () -> Unit,
) {
    Column(Modifier
        .padding(16.dp)
        .navigationBarsPadding()) {
        TopBar(title = "Void")
        VerticalSpacer(SpacerSize.X_LARGE)

        PrimaryTextField(
            value = traceNo,
            onValueChange = onTraceNoChanged,
            label = "Trace No"
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