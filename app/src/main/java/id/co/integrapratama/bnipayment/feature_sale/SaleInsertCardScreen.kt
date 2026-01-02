package id.co.integrapratama.bnipayment.feature_sale

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.bnipayment.common.spacer.SpacerSize
import id.co.integrapratama.bnipayment.common.spacer.VerticalSpacer
import id.co.integrapratama.bnipayment.common.ui_component.TopBar
import id.co.integrapratama.bnipayment.ui.theme.PrimaryButtonColor

@Composable
fun InsertCardScreen(
    isReading: Boolean = false,
    onNavigationBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopBar(title = "Sale", onBackClick = onNavigationBack)

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painterResource(R.drawable.ic_insert_card), "")

            VerticalSpacer(SpacerSize.LARGE)

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = if (isReading)
                    "Membaca kartu..."
                else
                    "Gesek kartu debit nasabah Anda pada magnetik reader atau masukkan pada bagian bawah mesin EDC Android",
                textAlign = TextAlign.Center
            )

            if (isReading) {
                VerticalSpacer(SpacerSize.LARGE)
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp), color = PrimaryButtonColor
                )
            }
        }
    }
}