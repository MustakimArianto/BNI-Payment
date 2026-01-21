package id.co.integrapratama.bnipayment.common.ui_component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    @DrawableRes
    iconRes: Int? = null,
    yesButtonText: String = "Yes",
    noButtonText: String = "No",
    onYesButtonClicked: () -> Unit,
    onNoButtonClicked: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row {
                    if (iconRes != null) {
                        AsyncImage(
                            modifier = Modifier.size(28.dp),
                            model = iconRes,
                            contentDescription = "Confirmation Dialog Icon"
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Start,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = message,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = Modifier.height(16.dp))
                PrimaryButton(
                    text = yesButtonText,
                    onClick = onYesButtonClicked,
                    modifier = Modifier.fillMaxWidth()
                )
                PrimaryButton(
                    text = noButtonText,
                    onClick = onNoButtonClicked,
                    modifier = Modifier.fillMaxWidth(),
                    appearance = PrimaryButtonAppearance.Outline
                )
            }
        }
    }
}