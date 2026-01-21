package id.co.integrapratama.bnipayment.feature_void

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.bnipayment.common.ui_component.CardNumberCard
import id.co.integrapratama.bnipayment.common.ui_component.PrimaryButton
import id.co.integrapratama.bnipayment.common.ui_component.TopBar
import id.co.integrapratama.bnipayment.common.ui_component.TransactionDetailRow
import id.co.integrapratama.bnipayment.common.ui_component.spacer.SpacerSize
import id.co.integrapratama.bnipayment.common.ui_component.spacer.VerticalSpacer
import id.co.integrapratama.bnipayment.ui.theme.LightGray
import id.co.integrapratama.sdk.core.utils.DateUtils
import id.co.integrapratama.sdk.core.utils.StringUtil
import id.co.integrapratama.sdk.feature_void.domain.ListVoidModel

@Composable
fun VoidConfirmTransactionScreen(
    transaction: ListVoidModel,
    maskedCardNo: String,
    cardName: String,
    transactionDate: String,
    transactionTime: String,
    refNo: String,
    mid: String,
    method: String,
    amount: String,
    tip: String,
    traceNo: String,
    totalAmount: String,
    onVoidTransaction: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(title = "Trace No. ${traceNo.padStart(6, '0')}")
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .drawBehind {
                        drawLine(
                            color = LightGray,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                    .padding(16.dp)) {
                PrimaryButton(
                    text = stringResource(R.string.text_void_transaction),
                    onClick = onVoidTransaction
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            CardNumberCard(maskedCardNo, transaction.aidName)
            VerticalSpacer(SpacerSize.X_LARGE)
            TransactionDetailRow("Card Name", cardName)
            VerticalSpacer(SpacerSize.MEDIUM)
            TransactionDetailRow(
                "Transaction date",
                DateUtils.getScreenReceiptTransactionDate(transactionDate)
            )
            VerticalSpacer(SpacerSize.MEDIUM)
            TransactionDetailRow(
                "Transaction time",
                DateUtils.getScreenReceiptTransactionTime(transactionTime)
            )
            VerticalSpacer(SpacerSize.MEDIUM)
            TransactionDetailRow("Trace Number", traceNo.padStart(6, '0'))
            VerticalSpacer(SpacerSize.MEDIUM)
            TransactionDetailRow("Reff Number", refNo)
            VerticalSpacer(SpacerSize.MEDIUM)
            TransactionDetailRow("MID", mid)
            VerticalSpacer(SpacerSize.MEDIUM)
            TransactionDetailRow("Method", method)
            VerticalSpacer(SpacerSize.MEDIUM)
            TransactionDetailRow("Purchase Amount", StringUtil.formatRupiahCurrency(amount))
            VerticalSpacer(SpacerSize.MEDIUM)
            TransactionDetailRow("Tip", StringUtil.formatRupiahCurrency(tip))
            VerticalSpacer(SpacerSize.MEDIUM)
            TransactionDetailRow("Total Amount", StringUtil.formatRupiahCurrency(totalAmount))
        }
    }
}