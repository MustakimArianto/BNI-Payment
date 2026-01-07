package id.co.integrapratama.sdk.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    private val locale = Locale.ROOT
    val dateFormat = SimpleDateFormat("MMdd", locale)
    val dateTimeFormat = SimpleDateFormat("MMddHHmmss", locale)
    val timeFormat = SimpleDateFormat("HHmmss", locale)

    fun getCurrentTransactionDateTime(): String {
        return dateTimeFormat.format(System.currentTimeMillis())
    }

    fun getTransactionDate(dateTime: String): String {
        return dateFormat.format(dateTimeFormat.parse(dateTime) ?: Date())
    }

    fun getTransactionTime(dateTime: String): String {
        return timeFormat.format(dateTimeFormat.parse(dateTime) ?: Date())
    }
}