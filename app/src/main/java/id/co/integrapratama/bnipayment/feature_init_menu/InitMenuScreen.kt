package id.co.integrapratama.bnipayment.feature_init_menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.bnipayment.common.ui_component.CircularLoader
import id.co.integrapratama.bnipayment.common.ui_component.SecondaryButton
import id.co.integrapratama.bnipayment.common.ui_component.spacer.SpacerSize
import id.co.integrapratama.bnipayment.common.ui_component.spacer.VerticalSpacer
import id.co.integrapratama.bnipayment.ui.theme.DividerColor
import id.co.integrapratama.bnipayment.ui.theme.TextGrayColor

@Composable
fun InitMenuScreen(
    loadingMessage: String,
    errorMessage: String,
    currentStep: Int,
    maxStep: Int,
    isLoading: Boolean,
    isInitSuccess: Boolean,
    isInitFailed: Boolean,
    onInitSuccess: () -> Unit,
    onInitFailed: () -> Unit,
    onInitializeClick: () -> Unit
) {
    Column(Modifier.systemBarsPadding()) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = "Initialized Menu",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
        VerticalSpacer(SpacerSize.LARGE)
        HorizontalDivider(thickness = 3.dp, color = DividerColor)
        VerticalSpacer(SpacerSize.X_LARGE)
        if (isLoading) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Initializing Device",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
                VerticalSpacer(SpacerSize.X_LARGE)
                CircularLoader(R.drawable.ic_initialize_loading)
                VerticalSpacer(SpacerSize.X_LARGE)
                Text(
                    text = loadingMessage,
                    color = TextGrayColor,
                )
                Text(
                    text = "Download packet : $currentStep/$maxStep",
                    color = TextGrayColor,
                )
            }
        } else if (isInitSuccess) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Success",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
                VerticalSpacer(SpacerSize.X_LARGE)
                Image(
                    modifier = Modifier.size(150.dp),
                    painter = painterResource(R.drawable.ic_check_circle_orange_24),
                    contentDescription = null
                )
                VerticalSpacer(SpacerSize.X_LARGE)
            }
            onInitSuccess()
        } else if (isInitFailed) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Failed",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
                VerticalSpacer(SpacerSize.X_LARGE)
                Image(
                    modifier = Modifier.size(150.dp),
                    painter = painterResource(R.drawable.outline_cancel_24),
                    contentDescription = null
                )
                VerticalSpacer(SpacerSize.X_LARGE)
                Text(
                    text = errorMessage,
                    color = TextGrayColor,
                )
            }
            onInitFailed()
        } else {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_init_device),
                    contentDescription = null
                )
                VerticalSpacer(SpacerSize.LARGE)
                Text(
                    text = "Please initialize first",
                    fontSize = 22.sp,
                    color = TextGrayColor,
                    fontWeight = FontWeight.SemiBold
                )
                VerticalSpacer(SpacerSize.X_LARGE)
                VerticalSpacer(SpacerSize.X_LARGE)
                SecondaryButton(
                    text = "Initialize This Device",
                    isBold = true,
                    onClick = onInitializeClick
                )
            }
        }
    }
}
