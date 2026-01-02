package id.co.integrapratama.bnipayment.common.ui_component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import id.co.integrapratama.bnipayment.common.formatCurrency
import id.co.integrapratama.bnipayment.common.unformatCurrency

/**
 * Composable function to manage currency input with proper cursor positioning
 * Keeps TextFieldValue in UI layer while syncing with ViewModel's raw value
 * 
 * @param rawValue The unformatted value from ViewModel (e.g., "5000")
 * @param onValueChange Callback with unformatted value for ViewModel
 * @return TextFieldValue with formatted text and correct cursor position
 */
@Composable
fun rememberCurrencyTextFieldValue(
    rawValue: String,
    onValueChange: (String) -> Unit
): Pair<TextFieldValue, (TextFieldValue) -> Unit> {
    var textFieldValue by remember(rawValue) {
        mutableStateOf(
            TextFieldValue(
                text = formatCurrency(rawValue),
                selection = TextRange(formatCurrency(rawValue).length)
            )
        )
    }

    val handleValueChange: (TextFieldValue) -> Unit = { newValue ->
        val cleanText = unformatCurrency(newValue.text)
        
        // Only accept numeric input
        if (cleanText.isEmpty() || cleanText.all { it.isDigit() }) {
            val formatted = formatCurrency(cleanText)
            
            // Calculate new cursor position
            val oldText = textFieldValue.text
            val oldCursor = newValue.selection.start
            
            // Count dots before cursor in old text
            val dotsBeforeInOld = oldText.take(oldCursor.coerceAtMost(oldText.length)).count { it == '.' }
            
            // Find equivalent position in clean text
            val cleanCursor = oldCursor - dotsBeforeInOld
            
            // Count dots before that position in new formatted text
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
            
            onValueChange(cleanText)
        }
    }

    return textFieldValue to handleValueChange
}