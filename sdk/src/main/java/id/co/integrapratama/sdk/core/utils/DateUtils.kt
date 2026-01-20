package id.co.integrapratama.sdk.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    private val locale = Locale.forLanguageTag("id-ID")
    val fullDateTimeFormat = SimpleDateFormat("yyyyMMddHHmmss", locale)
    val dateFormat = SimpleDateFormat("MMdd", locale)
    val dateTimeFormat = SimpleDateFormat("MMddHHmmss", locale)
    val timeFormat = SimpleDateFormat("HHmmss", locale)
    val receiptDateFormat = SimpleDateFormat("dd MMM yyyy", locale)
    val receiptTimeFormat = SimpleDateFormat("HH:mm:ss", locale)

    val timeFormatScreenReceipt = SimpleDateFormat("HH:mm", locale)
    val dateFormatScreenReceipt = SimpleDateFormat("dd MMM yyyy", locale)

    fun getFullTransactionDateTime(date: Date): String {
        return fullDateTimeFormat.format(date)
    }

    fun getTransactionDateTime(date: Date): String {
        return dateTimeFormat.format(date)
    }

    fun getCurrentTransactionDateTime(): String {
        return fullDateTimeFormat.format(Date())
    }

    fun getTransactionDate(dateTime: String): String {
        return dateFormat.format(fullDateTimeFormat.parse(dateTime) ?: Date())
    }

    fun getTransactionTime(dateTime: String): String {
        return timeFormat.format(fullDateTimeFormat.parse(dateTime) ?: Date())
    }

    fun getTransactionDate(date: Date): String {
        return dateFormat.format(date)
    }

    fun getTransactionTime(date: Date): String {
        return timeFormat.format(date)
    }

    fun getReceiptTransactionDate(date: Date): String {
        return receiptDateFormat.format(date)
    }

    fun getReceiptTransactionTime(date: Date): String {
        return receiptTimeFormat.format(date)
    }

    fun getScreenReceiptTransactionDate(date: Date): String {
        return dateFormatScreenReceipt.format(date)
    }

    fun getScreenReceiptTransactionTime(date: Date): String {
        return timeFormatScreenReceipt.format(date) + " WIB"
    }
}