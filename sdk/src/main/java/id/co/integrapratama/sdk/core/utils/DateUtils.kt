package id.co.integrapratama.sdk.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    private val locale = Locale.forLanguageTag("id-ID")
    val fullDateFormat = SimpleDateFormat("yyyyMMdd", locale)
    val fullDateTimeFormat = SimpleDateFormat("yyyyMMddHHmmss", locale)
    val fullTimeFormat = SimpleDateFormat("HHmmss", locale)
    val dateFormat = SimpleDateFormat("MMdd", locale)
    val dateTimeFormat = SimpleDateFormat("MMddHHmmss", locale)
    val timeFormat = SimpleDateFormat("HHmmss", locale)
    val receiptDateFormat = SimpleDateFormat("dd MMM yyyy", locale)
    val receiptTimeFormat = SimpleDateFormat("HH:mm:ss", locale)

    fun getCurrentFullTransactionDateTime(): String {
        return fullDateTimeFormat.format(System.currentTimeMillis())
    }

    fun getFullTransactionDate(dateTime: String): String {
        return fullDateFormat.format(fullDateTimeFormat.parse(dateTime) ?: Date())
    }

    fun getFullTransactionTime(dateTime: String): String {
        return fullTimeFormat.format(fullDateTimeFormat.parse(dateTime) ?: Date())
    }

    fun getFullTransactionDateTime(date: Date): String {
        return fullDateTimeFormat.format(date)
    }

    fun getFullTransactionDate(date: Date): String {
        return fullDateFormat.format(date)
    }

    fun getFullTransactionTime(date: Date): String {
        return fullTimeFormat.format(date)
    }

    fun getCurrentTransactionDateTime(): String {
        return dateTimeFormat.format(System.currentTimeMillis())
    }

    fun getTransactionDate(dateTime: String): String {
        return dateFormat.format(dateTimeFormat.parse(dateTime) ?: Date())
    }

    fun getTransactionTime(dateTime: String): String {
        return timeFormat.format(dateTimeFormat.parse(dateTime) ?: Date())
    }

    fun getTransactionDate(date: Date): String {
        return timeFormat.format(date)
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
}