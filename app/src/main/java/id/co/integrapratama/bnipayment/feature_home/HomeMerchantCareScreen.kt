package id.co.integrapratama.bnipayment.feature_home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.bnipayment.common.ui_component.spacer.HorizontalSpacer
import id.co.integrapratama.bnipayment.common.ui_component.spacer.SpacerSize
import id.co.integrapratama.bnipayment.common.ui_component.spacer.VerticalSpacer
import id.co.integrapratama.bnipayment.ui.theme.DividerColor
import id.co.integrapratama.bnipayment.ui.theme.TextGrayColor

@Composable
fun HomeMerchantCareScreen(
) {
    Column(
        Modifier
            .systemBarsPadding(),
    ) {
        VerticalSpacer(SpacerSize.LARGE)
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = "Merchant Care",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
        VerticalSpacer(SpacerSize.LARGE)
        HorizontalDivider(thickness = 3.dp, color = DividerColor)
        VerticalSpacer(SpacerSize.X_LARGE)
        Image(
            modifier = Modifier.padding(horizontal = 16.dp),
            painter = painterResource(R.drawable.ic_merchant_care),
            contentDescription = null
        )
        VerticalSpacer(SpacerSize.X_LARGE)
        ContactSection("1500146", "bnicall@bni.co.id")
    }
}

@Composable
private fun ContactSection(phoneNumber: String, email: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Image(
            modifier = Modifier.size(32.dp),
            painter = painterResource(R.drawable.ic_call_primary_24),
            contentDescription = null
        )

        HorizontalSpacer(SpacerSize.LARGE)
        Column {
            Text(
                text = "BNI Merchant Center",
                color = TextGrayColor,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            VerticalSpacer(SpacerSize.SMALL)
            Text(text = phoneNumber, color = TextGrayColor)
            VerticalSpacer(SpacerSize.LARGE)
        }
    }
    HorizontalDivider(thickness = 1.dp, color = DividerColor)
    VerticalSpacer(SpacerSize.LARGE)

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Image(
            modifier = Modifier.size(32.dp),
            painter = painterResource(R.drawable.ic_outline_email_primary_24),
            contentDescription = null
        )
        HorizontalSpacer(SpacerSize.LARGE)
        Column {
            Text(
                text = "Email BNI",
                color = TextGrayColor,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            VerticalSpacer(SpacerSize.SMALL)
            Text(text = email, color = TextGrayColor)
            VerticalSpacer(SpacerSize.LARGE)
        }
    }

    HorizontalDivider(thickness = 1.dp, color = DividerColor)
}