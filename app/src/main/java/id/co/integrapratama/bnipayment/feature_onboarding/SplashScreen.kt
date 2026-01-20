package id.co.integrapratama.bnipayment.feature_onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import id.co.integrapratama.bnipayment.R
import id.co.integrapratama.bnipayment.common.ui_component.spacer.SpacerSize
import id.co.integrapratama.bnipayment.common.ui_component.spacer.VerticalSpacer

@Composable
fun SplashScreen(
    version: String
) {
    Box(Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.ic_splash_bg),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )

        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VerticalSpacer(SpacerSize.X_LARGE)
            Image(painterResource(R.drawable.ic_splash_logo), contentDescription = "")
            VerticalSpacer(SpacerSize.X_LARGE)
            Text(text = "Ver. $version", color = Color.White)
            VerticalSpacer(SpacerSize.X_LARGE)
        }
    }
}