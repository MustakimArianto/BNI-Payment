package id.co.integrapratama.bnipayment.common.ui_component

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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import id.co.integrapratama.bnipayment.common.formatCurrency
import id.co.integrapratama.bnipayment.common.unformatCurrency
import id.co.integrapratama.bnipayment.ui.theme.TextGrayColor

@Composable
fun InputAmountTextField(
    modifier: Modifier = Modifier,
    amount: String,
    onAmountChanged: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit = {},
) {
    val maxLength = 12

    var textFieldValue by remember {
        mutableStateOf(TextFieldValue())
    }

    var lastAmount by remember { mutableStateOf(amount) }
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(amount) {
        if (amount != lastAmount) {
            lastAmount = amount
            val formatted = if (amount.isNotEmpty()) {
                "Rp${formatCurrency(amount)}"
            } else {
                ""
            }
            textFieldValue = TextFieldValue(
                text = formatted,
                selection = TextRange(formatted.length)
            )
        }
    }

    val textColor = if (textFieldValue.text.isEmpty() || textFieldValue.text == "Rp") {
        TextGrayColor
    } else {
        Color.Black
    }

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
                onFocusChanged(focusState.isFocused)
            },
        value = textFieldValue,
        onValueChange = { newValue ->
            var inputText = newValue.text

            // Remove "Rp" prefix for processing
            if (inputText.startsWith("Rp")) {
                inputText = inputText.substring(2)
            }

            val cleanText = unformatCurrency(inputText)

            // Only accept digits and respect max length
            if ((cleanText.isEmpty() || cleanText.all { it.isDigit() }) && cleanText.length <= maxLength) {
                val formatted = if (cleanText.isNotEmpty()) {
                    "Rp${formatCurrency(cleanText)}"
                } else {
                    ""
                }

                // Calculate cursor position
                val newCursor = if (formatted.isEmpty()) {
                    0
                } else {
                    val oldTextWithoutPrefix = textFieldValue.text.removePrefix("Rp")
                    val newTextWithoutPrefix = formatted.substring(2)

                    // Get cursor position relative to "Rp" prefix
                    val oldCursorPos = (newValue.selection.start - 2).coerceAtLeast(0)

                    // Count dots before cursor in old text
                    val dotsBeforeInOld = oldTextWithoutPrefix
                        .take(oldCursorPos.coerceAtMost(oldTextWithoutPrefix.length))
                        .count { it == '.' }

                    // Get clean cursor position (without dots)
                    val cleanCursorPos = oldCursorPos - dotsBeforeInOld

                    // Find new cursor position in formatted text
                    var newCursorPos = 0
                    var cleanCount = 0

                    for (i in newTextWithoutPrefix.indices) {
                        if (cleanCount >= cleanCursorPos) break
                        if (newTextWithoutPrefix[i] != '.') {
                            cleanCount++
                        }
                        newCursorPos++
                    }

                    // Add 2 for "Rp" prefix
                    (newCursorPos + 2).coerceIn(2, formatted.length)
                }

                textFieldValue = TextFieldValue(
                    text = formatted,
                    selection = TextRange(newCursor)
                )

                lastAmount = cleanText

                // Send raw digits only to ViewModel
                onAmountChanged(cleanText)
            }
        },
        textStyle = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        ),
        placeholder = {
            if (!isFocused || textFieldValue.text.isEmpty()) {
                Text(
                    text = "Rp",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextGrayColor
                )
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
        ),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.LightGray,
            focusedIndicatorColor = Color.LightGray,
            cursorColor = Color.Black,
            disabledContainerColor = Color.Transparent,
            disabledIndicatorColor = Color.LightGray
        ),
        singleLine = true,
        readOnly = true
    )
}