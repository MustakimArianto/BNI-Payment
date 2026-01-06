package id.co.integrapratama.bnipayment.common.ui_component.spacer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalSpacer(spacerSize: SpacerSize) {
    when (spacerSize) {
        SpacerSize.X_SMALL -> {
            Spacer(modifier = Modifier.width(2.dp))
        }

        SpacerSize.SMALL -> {
            Spacer(modifier = Modifier.width(4.dp))
        }

        SpacerSize.MEDIUM -> {
            Spacer(modifier = Modifier.width(8.dp))
        }

        SpacerSize.LARGE -> {
            Spacer(modifier = Modifier.width(16.dp))
        }

        SpacerSize.X_LARGE -> {
            Spacer(modifier = Modifier.width(32.dp))
        }
    }
}