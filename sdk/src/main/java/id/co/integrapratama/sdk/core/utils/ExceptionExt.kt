package id.co.integrapratama.sdk.core.utils

import android.database.sqlite.SQLiteException
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun <T> Exception.toResourceError(
    customMessage: String? = null
): Resource.Error<T> {
    val message = customMessage ?: when (this) {
        is SQLiteException -> "Terjadi kesalahan saat mengakses database"
        is UnknownHostException -> "Tidak ada koneksi Internet"
        is ConnectException -> "Tidak dapat terhubung ke server"
        is SocketTimeoutException -> "Koneksi Timeout"
        is IOException -> "Terjadi kesalahan jaringan"
        else -> "Terjadi kesalahan"
    }
    return Resource.Error(message)
}