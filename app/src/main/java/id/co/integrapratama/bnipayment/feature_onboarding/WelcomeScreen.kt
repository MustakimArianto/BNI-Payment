package id.co.integrapratama.bnipayment.feature_onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.bnipayment.common.ui_component.PrimaryButton
import id.co.integrapratama.bnipayment.common.ui_component.spacer.SpacerSize
import id.co.integrapratama.bnipayment.common.ui_component.spacer.VerticalSpacer
import id.co.integrapratama.bnipayment.ui.theme.DividerColor
import id.co.integrapratama.bnipayment.ui.theme.PrimaryColor

@Composable
fun WelcomeScreen(onEnterClick: () -> Unit) {
    Column(Modifier.systemBarsPadding(), horizontalAlignment = Alignment.CenterHorizontally) {
        VerticalSpacer(SpacerSize.LARGE)
        Image(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .align(Alignment.Start),
            painter = painterResource(R.drawable.ic_bni_topbar_logo),
            contentDescription = ""
        )
        VerticalSpacer(SpacerSize.LARGE)
        HorizontalDivider(thickness = 3.dp, color = DividerColor)
        VerticalSpacer(SpacerSize.X_LARGE)
        VerticalSpacer(SpacerSize.X_LARGE)
        Image(painter = painterResource(R.drawable.ic_welcome_image), contentDescription = "")
        VerticalSpacer(SpacerSize.X_LARGE)
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Welcome to EDC BNI",
            textAlign = TextAlign.Center,
            color = PrimaryColor,
            fontWeight = FontWeight.SemiBold
        )
        VerticalSpacer(SpacerSize.MEDIUM)
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Seamless transaction device for all payment",
            textAlign = TextAlign.Center,
            color = PrimaryColor
        )
        Box(Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            PrimaryButton(
                modifier = Modifier.align(Alignment.BottomCenter),
                text = "Enter",
                isBold = true,
                onClick = {
                    onEnterClick()
                }
            )
            VerticalSpacer(SpacerSize.X_LARGE)
        }
    }
}