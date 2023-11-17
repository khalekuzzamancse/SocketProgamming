package org.example.server

import org.example.BytesToFileWriter
import org.example.Peer
import org.example.TextEncoderDecoder
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.ServerSocket
import java.net.Socket


class Server(
    port: Int = Peer.SERVER_PORT
) : Peer {
    private val serverSocket = ServerSocket(port)
    private val connectedClients = mutableListOf<ClientHandler>()
    private var socket = Socket()

    companion object {
        const val TAG = "Server:: "
    }

    fun start() {
        println("Server:running")
        while (true) {
            try {
                val clientSocket = serverSocket.accept()
                socket = clientSocket
                connectedClients.add(ClientHandler())
                //  observeReceiveData()
                // readBytes()
               // anyDataReadingWritingDemo()
                readWriteBytes()
            } catch (_: Exception) {

            }

        }

    }

    private fun anyDataReadingWritingDemo() {
        val path = "C:\\Users\\Khalekuzzaman\\Desktop\\socket\\clinet\\newimg.jpg"
        val outputStream = FileOutputStream(path)

        readBytes(onReading = { receivedPacket ->
            println(receivedPacket.size)
            outputStream.write(receivedPacket)
        }, onFinished = {
            outputStream.close()
            println("Finished")
        }
        )
    }

    override suspend fun send(data: ByteArray): Boolean {
        TODO("Not yet implemented")
    }


    override fun observeReceiveData() {
        val closeSignal = -1
        var shouldRead = true
        val dataBytes = mutableListOf<Byte>()
        try {
            val inputStream: InputStream = socket.getInputStream()
            while (shouldRead) {
                val bytes = inputStream.read()
                if (bytes != closeSignal)
                    dataBytes.add(bytes.toByte())
                if (bytes == closeSignal) {
                    inputStream.close()
                    shouldRead = false
                }

            }
            println("${TextEncoderDecoder().decode(dataBytes.toByteArray())} ")

        } catch (e: IOException) {
            println("$TAG observeReceiveData() causes exception")
        }
    }

    private fun readBytes(
        onReading: (ByteArray) -> Unit = {},
        onFinished: () -> Unit = {}
    ) {
        val closeSignal = -1
        var shouldRead = true
        val maxByteToRead = 1024
        val buffer = ByteArray(maxByteToRead)
        try {
            val inputStream: InputStream = socket.getInputStream()
            while (shouldRead) {
                val numberOfByteWasRead = inputStream.read(buffer)
                if (numberOfByteWasRead != closeSignal) {
                    val actualReadBytes = buffer.copyOf(numberOfByteWasRead)
                    onReading(actualReadBytes)
                }
                if (numberOfByteWasRead == closeSignal) {
                    inputStream.close()
                    shouldRead = false
                    onFinished()
                }
            }

            //  println("${TextEncoderDecoder().decode(dataBytes.toByteArray())} ")

        } catch (e: IOException) {
            println("$TAG observeReceiveData() causes exception")
        }
    }
    private fun readWriteBytes(

    ) {
        val path = "C:\\Users\\Khalekuzzaman\\Desktop\\socket\\clinet\\newimg2.jpg"
        val outputStream = FileOutputStream(path)
        //
        val closeSignal = -1
        var shouldRead = true
        val maxByteToRead = 1024
        val buffer = ByteArray(maxByteToRead)
        try {
            val inputStream: InputStream = socket.getInputStream()
            while (shouldRead) {
                val numberOfByteWasRead = inputStream.read(buffer)
                if (numberOfByteWasRead != closeSignal) {
                    val actualReadBytes = buffer.copyOf(numberOfByteWasRead)
                    outputStream.write(buffer,0,numberOfByteWasRead)

                }
                if (numberOfByteWasRead == closeSignal) {
                    inputStream.close()
                    shouldRead = false
                    outputStream.close()

                }
            }

            //  println("${TextEncoderDecoder().decode(dataBytes.toByteArray())} ")

        } catch (e: IOException) {
            println("$TAG observeReceiveData() causes exception")
        }
    }


}


class ClientHandler