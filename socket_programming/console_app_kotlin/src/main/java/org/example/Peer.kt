package org.example

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.DataOutputStream
import java.io.IOException
import java.net.Socket


interface Peer {
    companion object {
        const val SERVER_PORT = 43234
    }

    suspend fun send(data: ByteArray): Boolean
    fun observeReceiveData()
}


class DataSender(
    private val type: FileExtension,
    private val socket: Socket,
    private val sender: Sender = Sender(type)
) {

    companion object {
        const val TAG = "DataSender::"
    }

    suspend fun sendHeader(): Boolean {
//        val success = send(sender.encodedFileType())
        val success = send(ByteArray(20))
        if (!success) {
            print("$TAG sendHeader() failed")
        }
        return success
    }

    suspend fun sendData(oneByte: Byte): Boolean {
        val success = send(byteArrayOf(oneByte))
        if (!success) {
            print("$TAG sendData() failed")
        }
        return success
    }

    suspend fun sendEndFlag(): Boolean {
//        val success = send(sender.encodeEndFlag())
        val success = send(ByteArray(15))
        if (!success) {
            print("$TAG sendEndFlag() failed")
        }
        return success
    }

    private suspend fun send(data: ByteArray): Boolean = withContext(Dispatchers.IO) {
        var res = true
        try {
            val outputStream = DataOutputStream(socket.getOutputStream())
            outputStream.write(data)
            outputStream.flush()
            outputStream.close()

        } catch (e: IOException) {
            res = false
            print("$TAG send() causes IOException")
        }
        res
    }


}

