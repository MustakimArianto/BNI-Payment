package id.co.integrapratama.bnipayment.feature_sale

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.bnipayment.common.ui_component.CardNumberCard
import id.co.integrapratama.bnipayment.common.ui_component.TransactionDetailRow
import id.co.integrapratama.bnipayment.common.ui_component.spacer.SpacerSize
import id.co.integrapratama.bnipayment.common.ui_component.spacer.VerticalSpacer
import id.co.integrapratama.sdk.core.model.AIDName

@Composable
fun SaleScreenReceiptScreen(
    cardNumber: String,
    aidName: AIDName,
    merchantName: String,
    tid: String,
    traceNumber: String,
    reffNumber: String,
    tip: String,
    amount: String,
    total: String
) {
    Column(Modifier.padding(16.dp)) {
        CardNumberCard(cardNumber, aidName)
        VerticalSpacer(SpacerSize.X_LARGE)
        TransactionDetailRow(stringResource(R.string.label_merchant_name), merchantName)
        TransactionDetailRow("TID", tid)
        TransactionDetailRow("Trace Number", traceNumber)
        TransactionDetailRow("Reff Number", reffNumber)
        TransactionDetailRow("Purchase Amount", amount)
        TransactionDetailRow("Tip", tip)
        TransactionDetailRow("Total Paid", total)
    }
}