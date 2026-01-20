package id.co.integrapratama.bnipayment.feature_settlement

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import id.co.integrapratama.bnipayment.common.ui_component.PrimaryButton
import id.co.integrapratama.bnipayment.common.ui_component.TopBar
import id.co.integrapratama.bnipayment.common.ui_component.spacer.SpacerSize
import id.co.integrapratama.bnipayment.common.ui_component.spacer.VerticalSpacer
import id.co.integrapratama.sdk.feature_settlement.domain.TotalSettlementSummaryModel
import id.co.payment2go.terminalsdkhelper.core.util.Resource

@Composable
fun TotalSettlementSummaryScreen(
    totalSettlementSummaryModelResult: Resource<TotalSettlementSummaryModel>,
    onBackClick: () -> Unit,
    onStartSettlement: () -> Unit
) {
    Column {
        BackHandler {
            onBackClick()
        }

        TopBar(title = "Settlement")
        VerticalSpacer(SpacerSize.X_LARGE)

        TotalSettlementSummaryView(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 16.dp)
                .weight(1f),
            totalSettlementSummaryModelResult = totalSettlementSummaryModelResult
        )

        Box {
            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
                    .align(Alignment.BottomCenter),
                text = "Start Settlement",
                onClick = onStartSettlement
            )
        }
    }
}