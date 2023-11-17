package org.example.server

import org.example.PacketToFileWriter
import org.example.Peer
import java.io.IOException
import java.io.InputStream
import java.net.ServerSocket
import java.net.Socket


class Server(
    port: Int = Peer.SERVER_PORT
) : Peer {
    private val serverSocket = ServerSocket(port)
    private var socket = Socket()

    companion object {
        const val TAG = "Server:: "
    }

    fun start() {
        println("Server:running")
        //while (true) {
        //infinite loop can cause error to writing files
        //same file can be override multiple time with dummy value because of infinite loop
        try {
            val clientSocket = serverSocket.accept()
            socket = clientSocket
            //   textWriterDemo()
            // imageWriterDemo()
            videoWriterDemo()

        } catch (_: Exception) {
        }

        // }

    }


    private fun videoWriterDemo() {
        val writer = PacketToFileWriter(
            directory = "C:\\Users\\Khalekuzzaman\\Desktop\\socket",
            fileName = "receivedVideo",
            extension = "mp4"
        )
        readAndWrite(writer)
    }

    private fun imageWriterDemo() {
        val writer = PacketToFileWriter(
            directory = "C:\\Users\\Khalekuzzaman\\Desktop\\socket",
            fileName = "newImage",
            extension = "jpg"
        )
        readAndWrite(writer)
    }

    private fun textWriterDemo() {
        val writer = PacketToFileWriter(
            directory = "C:\\Users\\Khalekuzzaman\\Desktop\\socket",
            fileName = "Hello5",
            extension = "txt"
        )
        readAndWrite(writer)
    }

    private fun readAndWrite(writer: PacketToFileWriter) {
        readPackets(
            onPacketReceived = writer::write,
            onFinished = writer::writeFinished
        )
    }


    override suspend fun send(data: ByteArray): Boolean {
        TODO("Not yet implemented")
    }


    override fun observeReceiveData() {

    }


    private fun readPackets(
        onPacketReceived: (ByteArray) -> Unit,
        onFinished: () -> Unit
    ) {
        val received = mutableListOf<Byte>()
        val closeSignal = -1
        var readingNotFinished = true
        val maxByteToRead = 1024
        val buffer = ByteArray(maxByteToRead)
        try {
            val inputStream: InputStream = socket.getInputStream()
            while (readingNotFinished) {
                val numberOfByteWasRead = inputStream.read(buffer)
                if (numberOfByteWasRead != closeSignal) {
                    val packet = buffer.copyOf(numberOfByteWasRead)
                    onPacketReceived(packet)
                    packet.forEach { received.add(it) }

                }
                if (numberOfByteWasRead == closeSignal) {
                    readingNotFinished = false
                    onFinished()
                }
            }
            inputStream.close()
        } catch (e: IOException) {
            println("$TAG readPackets() Causes Exception")
        }

    }
}
