package id.co.integrapratama.bnipayment.common.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import id.co.integrapratama.bnipayment.common.formatCurrency
import id.co.integrapratama.bnipayment.common.unformatCurrency

@Composable
fun InputAmountTextField(
    modifier: Modifier = Modifier,
    amount: String,  // Raw value from ViewModel: "5000"
    onAmountChanged: (String) -> Unit,  // Sends raw value back: "5000"
    label: String = "Nominal"
) {
    val maxLength = 12

    // Manage TextFieldValue internally
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue())
    }

    // Track the last known amount to detect external changes
    var lastAmount by remember { mutableStateOf(amount) }

    // Only update TextFieldValue when amount changes from OUTSIDE (not from user typing)
    LaunchedEffect(amount) {
        if (amount != lastAmount && amount != unformatCurrency(textFieldValue.text)) {
            lastAmount = amount
            val formatted = formatCurrency(amount)
            textFieldValue = TextFieldValue(
                text = formatted,
                selection = TextRange(formatted.length)
            )
        }
    }

    TextField(
        modifier = modifier.fillMaxWidth(),
        value = textFieldValue,
        onValueChange = { newValue ->
            val cleanText = unformatCurrency(newValue.text)

            // Only accept digits and respect max length
            if ((cleanText.isEmpty() || cleanText.all { it.isDigit() }) && cleanText.length <= maxLength) {
                val formatted = formatCurrency(cleanText)

                // Calculate new cursor position
                val oldText = textFieldValue.text
                val oldCursor = newValue.selection.start

                // Count dots before cursor in old text
                val dotsBeforeInOld =
                    oldText.take(oldCursor.coerceAtMost(oldText.length)).count { it == '.' }

                // Find equivalent position in clean text
                val cleanCursor = oldCursor - dotsBeforeInOld

                // Count characters (including dots) needed to reach that clean position in new formatted text
                var newCursor = 0
                var cleanCount = 0
                for (i in formatted.indices) {
                    if (cleanCount >= cleanCursor) break
                    if (formatted[i] != '.') cleanCount++
                    newCursor++
                }

                textFieldValue = TextFieldValue(
                    text = formatted,
                    selection = TextRange(newCursor.coerceIn(0, formatted.length))
                )

                lastAmount = cleanText

                // Send raw value to ViewModel
                onAmountChanged(cleanText)
            }
        },
        label = { Text(label) },
        prefix = { Text("Rp") },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
        ),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFE8F4F8),
            focusedContainerColor = Color(0xFFE8F4F8),
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            cursorColor = Color(0xFF5C6BC0),
            disabledContainerColor = Color(0xFFE8F4F8)
        ),
    )
}