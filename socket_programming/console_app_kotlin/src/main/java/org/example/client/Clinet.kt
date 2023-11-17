package org.example.client

import kotlinx.coroutines.*
import org.example.Peer
import org.example.fileRead
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


class Client(
    serverIP: String,
    serverPort: Int = Peer.SERVER_PORT
) : Peer {
    private var serverSocket: Socket? = null
    private val handler = ClientCommunicationHandler(serverIP, serverPort)

    private suspend fun connect() {
        serverSocket = handler.connect()
    }


    override suspend fun send(data: ByteArray): Boolean {
        if (isNotConnected()) {
            connect()
        }
        sendBytes(data.toList())
        closeConnection()
        reconnect()
        return true
    }

    suspend fun send(filePath: String) {
        if (isNotConnected()) {
            connect()
        }
        fileRead(filePath, onReading = { packet ->
              sendBytes(packet)
            println("Sent Packet: ${packet.toList()}")
        }, onReadingFinished = {
            closeConnection()
           // reconnect()
            println("Sent Finished")
        })

    }


    private fun isNotConnected(): Boolean {
        serverSocket ?: return true
        return false
    }

    private suspend fun sendBytes(bytes: ByteArray) {
        val socket = serverSocket
        if (socket == null) {
            println("Client:: send:Failed due to: socket is null")
            return
        }
        try {
            withContext(Dispatchers.IO) {
                val outputStream = socket.getOutputStream()
                outputStream.write(bytes)
            }

        } catch (_: IOException) {

        }
    }

    private suspend fun sendBytes(data: List<Byte>) {
        val socket = serverSocket
        if (socket == null) {
            println("Client:: send:Failed due to: socket is null")
            return
        }
        try {
            withContext(Dispatchers.IO) {
                val outputStream = socket.getOutputStream()
                //sending 1 byte at a time for desecration purpose though sending 1 byte poor performance
                //this concept is useful when sending large data then we can send
                //small chunks
//                data.forEach { oneByte ->
//                    outputStream.write(byteArrayOf(oneByte))
//                }
                outputStream.write(data.toByteArray())

            }

        } catch (_: IOException) {

        }
    }

    private suspend fun closeConnection() {
        serverSocket?.let { socket ->
            withContext(Dispatchers.IO) {
                socket.getOutputStream().close()
            }
        }
        serverSocket = null
    }

    private suspend fun reconnect() {
        connect()
    }

    override fun observeReceiveData() {
        TODO("Not yet implemented")
    }
}

class ClientCommunicationHandler(
    serverIP: String,
    serverPort: Int
) {
    companion object {
        const val TAG = "ClientCommunicationHandler:: "
    }

    private val serverAddress = InetSocketAddress(serverIP, serverPort)
    suspend fun connect(): Socket {
        while (true) {
            try {
                val socket = withContext(Dispatchers.IO) {
                    val newSocket = Socket()
                    newSocket.connect(serverAddress)
                    newSocket
                }

                if (socket.isConnected) {
                    println("Connected Successfully")
                    return socket
                } else {
                    withContext(Dispatchers.IO) {
                        socket.close()
                    }
                }
            } catch (ex: IOException) {
                // Delay before the next retry
                println("Connection Failed:Retrying")
                delay(1000)
            }
        }

    }

}

