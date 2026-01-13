package id.co.integrapratama.bnipayment.feature_void

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.bnipayment.common.ui_component.spacer.SpacerSize
import id.co.integrapratama.bnipayment.common.ui_component.spacer.VerticalSpacer
import id.co.integrapratama.bnipayment.common.ui_component.PrimaryButton
import id.co.integrapratama.bnipayment.common.ui_component.SecondaryButton

@Composable
fun VoidTransactionStatus(
    transactionResultMessage: String,
    onGoToHome: () -> Unit,
    onPrintReceipt: () -> Unit
) {
    if (transactionResultMessage.isEmpty()) {
        Box(
            Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(R.drawable.ic_bni_agen), "")
                VerticalSpacer(SpacerSize.X_LARGE)
                Image(painter = painterResource(R.drawable.ic_transaction_success), "")
                VerticalSpacer(SpacerSize.X_LARGE)
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Cetak struk berhasil dilakukan",
                    textAlign = TextAlign.Center
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondaryButton(
                    modifier = Modifier.weight(1f), text = "Beranda", onClick = onGoToHome
                )

                PrimaryButton(
                    modifier = Modifier.weight(1f), text = "Cetak", onClick = onPrintReceipt
                )
            }
        }
    } else {
        Box(
            Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(R.drawable.ic_bni_agen), "")
                VerticalSpacer(SpacerSize.X_LARGE)
                Image(painter = painterResource(R.drawable.ic_transaction_success), "")
                VerticalSpacer(SpacerSize.X_LARGE)
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Transaksi gagal",
                    textAlign = TextAlign.Center
                )
                VerticalSpacer(SpacerSize.MEDIUM)
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = transactionResultMessage,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
            }

            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                text = "Beranda",
                onClick = {
                    onGoToHome()
                })
        }
    }
}