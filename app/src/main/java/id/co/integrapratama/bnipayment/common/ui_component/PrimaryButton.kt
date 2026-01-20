package id.co.integrapratama.bnipayment.common.ui_component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.co.integrapratama.bnipayment.ui.theme.PrimaryVariantColor

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    isBold: Boolean = true,
    roundedCornerShape: RoundedCornerShape = RoundedCornerShape(22.dp),
    isEnabled: Boolean = true,
    appearance: PrimaryButtonAppearance = PrimaryButtonAppearance.Solid
) {
    Button(
        modifier = modifier
            .fillMaxWidth(),
        shape = roundedCornerShape,
        enabled = isEnabled,
        colors = appearance.buttonColors ?: ButtonDefaults.buttonColors(
            containerColor = PrimaryVariantColor,
            contentColor = Color.White,
        ),
        border = appearance.borderStroke,
        onClick = onClick
    ) {
        val fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal

        Text(text = text, fontWeight = fontWeight)
    }
}

@Preview(showSystemUi = true)
@Composable
fun PrimaryButtonPreview() {
    PrimaryButton(onClick = {}, text = "Masuk")
}

sealed class PrimaryButtonAppearance {
    @get:Composable
    abstract val buttonColors: ButtonColors?

    abstract val borderStroke: BorderStroke?

    object Solid : PrimaryButtonAppearance() {
        override val buttonColors: ButtonColors
            @Composable
            get() {
                return ButtonDefaults.buttonColors(
                    containerColor = PrimaryVariantColor,
                    contentColor = Color.White,
                )
            }

        override val borderStroke: BorderStroke?
            get() {
                return null
            }
    }

    object Outline : PrimaryButtonAppearance() {
        override val buttonColors: ButtonColors
            @Composable
            get() {
                return ButtonDefaults.buttonColors(
                    contentColor = PrimaryVariantColor,
                    containerColor = Color.Transparent
                )
            }

        override val borderStroke: BorderStroke
            get() {
                return BorderStroke(
                    width = 1.dp,
                    color = PrimaryVariantColor
                )
            }
    }
}