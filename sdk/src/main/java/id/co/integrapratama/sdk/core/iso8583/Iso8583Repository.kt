package id.co.integrapratama.sdk.core.iso8583

import android.util.Log
import id.co.integrapratama.iso8583sdk.IsoMessage
import id.co.integrapratama.iso8583sdk.IsoSpecConfiguration
import id.co.integrapratama.logsdk.LogSdk
import id.co.integrapratama.sdk.core.utils.toResourceError
import id.co.payment2go.terminalsdkhelper.core.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Iso8583Repository @Inject constructor(
    private val client: IsoSocketClient,
) {
    companion object {
        const val TAG = "Iso8583Repository"
    }

    fun sendAndReceive(
        message: ByteArray,
    ): Flow<Resource<ByteArray>> = flow {
        try {
            emit(Resource.Loading("Mengirim data ke host"))

            writeIsoLog(message, true)
            val response = withContext(Dispatchers.IO) { client.sendAndReceive(message) }

            if (response != null) {
                writeIsoLog(response, false)
                emit(Resource.Success(response))
            } else {
                writeIsoLog(byteArrayOf(), false)
                emit(Resource.Error("No response received from server"))
            }
        } catch (e: Exception) {
            LogSdk.error(TAG, e.stackTraceToString())
            emit(e.toResourceError())
        }
    }

    fun createRequest(mti: String, data: Map<Int, String>, spec: IsoSpecConfiguration = IsoConfig.genericSpec): ByteArray {
        val isoMessage = IsoMessage(spec).apply {
            setMTI(mti)
            data.forEach { (idx, value) ->
                setField(idx, value)
                Log.d("ISO8583", "Field $idx -> $value")
            }
        }

        Log.d("ISO8583", "MTI -> $mti")

        val packed = isoMessage.pack()
        Log.d("ISO8583", "Packed message (hex): ${packed.joinToString(" ") { "%02X".format(it) }}")

        return packed
    }

    fun writeIsoLog(message: ByteArray, isRequest: Boolean) {
        try {
            if (message.isEmpty()) {
                LogSdk.isoString(
                    if (isRequest) "Request" else "Response",
                    if (isRequest) "Request is empty" else "Response is empty"
                )
                return
            }

            val isoMessage = IsoMessage().unpack(message, IsoConfig.genericSpec)
            val mti = isoMessage.getMti()
            val messageString = StringBuilder()

            messageString.append("MTI: $mti\n")
            for (i in 2..isoMessage.getMaxField()) {
                if (isoMessage.hasField(i)) {
                    messageString.append("F${i}: ${isoMessage.getField(i)}\n")
                }
            }

            LogSdk.isoString(if (isRequest) "Request" else "Response", "$messageString")
        } catch (e: Exception) {
            LogSdk.error(TAG, "Error writing ISO log: ${e.message}")
            LogSdk.isoString(
                if (isRequest) "Request" else "Response",
                "Error parsing message: ${e.message}\nRaw bytes: ${message.joinToString(" ") { "%02X".format(it) }}"
            )
        }
    }
}