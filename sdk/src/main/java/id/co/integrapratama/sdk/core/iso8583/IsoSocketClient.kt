package id.co.integrapratama.sdk.core.iso8583

import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

class IsoSocketClient(
    private val host: String,
    private val port: Int
) {
    fun sendAndReceive(message: ByteArray): ByteArray? {
        var socket: Socket? = null
        var output: OutputStream? = null
        var input: InputStream? = null

        return try {
            // connect
            socket = Socket(host, port).apply {
                soTimeout = 10_000 // 10s read timeout
            }
            output = socket.getOutputStream()
            input = socket.getInputStream()

            // send
            val length = message.size
            val header = byteArrayOf(
                (length shr 8).toByte(),
                (length and 0xFF).toByte()
            )
            output.write(header)
            output.write(message)
            output.flush()

            // receive
            val headerBuf = ByteArray(2)
            if (!readFully(input, headerBuf, 0, 2)) return null

            val responseLength = ((headerBuf[0].toInt() and 0xFF) shl 8) or
                    (headerBuf[1].toInt() and 0xFF)

            val data = ByteArray(responseLength)
            if (!readFully(input, data, 0, responseLength)) return null

            data
        } finally {
            // always close resources
            try {
                input?.close()
            } catch (_: Exception) {
            }
            try {
                output?.close()
            } catch (_: Exception) {
            }
            try {
                socket?.close()
            } catch (_: Exception) {
            }
        }
    }

    private fun readFully(
        input: InputStream?,
        buffer: ByteArray,
        offset: Int,
        length: Int
    ): Boolean {
        var bytesRead = 0
        while (bytesRead < length) {
            val result = input?.read(buffer, offset + bytesRead, length - bytesRead) ?: -1
            if (result == -1) return false // stream closed
            bytesRead += result
        }
        return true
    }
}