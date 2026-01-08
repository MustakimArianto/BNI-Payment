package id.co.integrapratama.sdk.core.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object StringUtil {
    fun formatRupiahCurrency(amount: String): String {
        val value = amount.toLongOrNull() ?: 0L
        val symbols = DecimalFormatSymbols(Locale.forLanguageTag("id-ID")).apply {
            groupingSeparator = '.'
            decimalSeparator = ','
        }
        val formatter = DecimalFormat("#,###", symbols)
        return "Rp. ${formatter.format(value)}"
    }
}