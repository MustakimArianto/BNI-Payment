package id.co.integrapratama.bnipayment.common

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * Formats a number string with thousand separators
 * Example: "5000" -> "5.000"
 */
fun formatCurrency(amount: String): String {
    if (amount.isEmpty()) return ""

    val cleanAmount = amount.replace(".", "").replace(",", "")

    return try {
        val number = cleanAmount.toLongOrNull() ?: return amount
        val symbols = DecimalFormatSymbols(Locale.GERMANY)
        val formatter = DecimalFormat("#,###", symbols)
        formatter.format(number)
    } catch (e: Exception) {
        amount
    }
}

/**
 * Removes formatting from a number string
 * Example: "5.000" -> "5000"
 */
fun unformatCurrency(formattedAmount: String): String {
    return formattedAmount.replace(".", "").replace(",", "")
}