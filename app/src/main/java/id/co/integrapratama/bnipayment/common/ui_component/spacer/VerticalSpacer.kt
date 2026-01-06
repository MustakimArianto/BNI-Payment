package id.co.integrapratama.bnipayment.common.ui_component.spacer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VerticalSpacer(spacerSize: SpacerSize) {
    when (spacerSize) {
        SpacerSize.X_SMALL -> {
            Spacer(modifier = Modifier.height(2.dp))
        }

        SpacerSize.SMALL -> {
            Spacer(modifier = Modifier.height(4.dp))
        }

        SpacerSize.MEDIUM -> {
            Spacer(modifier = Modifier.height(8.dp))
        }

        SpacerSize.LARGE -> {
            Spacer(modifier = Modifier.height(16.dp))
        }

        SpacerSize.X_LARGE -> {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}