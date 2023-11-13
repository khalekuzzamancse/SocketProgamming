package org.example

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.DataOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.zip.DeflaterOutputStream



interface Peer {
    companion object {
        const val SERVER_PORT = 43234
    }

    suspend fun send(data: ByteArray): Boolean
    fun observeReceiveData()
}

enum class DataType {
    TextMessage, Image
}




class DataSender(
    private val dataType: DataType,
    private val socket: Socket
) {
    private val fileExtension = when (dataType) {
        DataType.Image -> "img"
        DataType.TextMessage -> "msg"
    }
    private val header = padStringTo20Bytes(fileExtension)
    private val footer = padStringTo20Bytes("@END")

    suspend fun sendHeader() {
        send(header.toByteArray())
    }

    suspend fun sendData(oneByte: Byte) {
        send(byteArrayOf(oneByte))
    }

    suspend fun sendFooter() {
        send(footer.toByteArray())
    }

    private suspend fun send(data: ByteArray) {
        try {
            withContext(Dispatchers.IO) {
                val outputStream = DataOutputStream(socket.getOutputStream())
                outputStream.write(data)
                outputStream.flush()
            }
        } catch (e: IOException) {
            // Handle the exception
        }
    }

    private fun padStringTo20Bytes(input: String): String {
        val utf8Bytes = input.toByteArray()
        val spacesToAdd = 10 - utf8Bytes.size
        return if (spacesToAdd >= 0) {
            val padding = " ".repeat(spacesToAdd)
            input + padding
        } else {
            String(utf8Bytes.copyOf(10))
        }
    }
}

fun main() {
    DataSender(DataType.Image, Socket())
}
