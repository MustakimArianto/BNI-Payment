package id.co.integrapratama.sdk.core.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.absoluteValue

object StringUtil {
    fun formatRupiahCurrency(amount: String): String {
        val value = amount.toLongOrNull() ?: 0L
        val symbols = DecimalFormatSymbols(Locale.forLanguageTag("id-ID")).apply {
            groupingSeparator = '.'
            decimalSeparator = ','
        }
        val formatter = DecimalFormat("#,###", symbols)
        return "${if (value < 0) "- " else ""}Rp${formatter.format(value.absoluteValue)}"
    }

    fun getRandom6DigitsNumber(): String {
        // Define the range for 6-digit numbers (inclusive)
        val min = 100000
        val max = 999999

        // Generate a random number within that range
        val random6DigitNumber = (min..max).random()
        return random6DigitNumber.toString()
    }
}