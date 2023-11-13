package org.example

import kotlinx.coroutines.*
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class Client(
    serverIP: String,
    serverPort: Int = Peer.SERVER_PORT
) : Peer {
    private var serverSocket: Socket? = null
    private val handler = ClientCommunicationHandler(serverIP, serverPort)

    suspend fun connect() {
        serverSocket = handler.connect()
    }

    override suspend fun send(data: ByteArray): Boolean {
        val socket = serverSocket ?: return false
        return handler.send(socket, data)
    }

    override fun observeReceiveData() {
        TODO("Not yet implemented")
    }
}

class ClientCommunicationHandler(
    serverIP: String,
    serverPort: Int
) {
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

    suspend fun send(socket: Socket, data: ByteArray): Boolean {
        if (socket.isConnected) {
//            try {
//                withContext(Dispatchers.IO) {
//                    socket.getOutputStream().write(data)
//                    socket.getOutputStream().flush()
//                }
//                return true
//            } catch (_: IOException) {
//            }
            val sender = DataSender(DataType.TextMessage, socket)
            sender.sendHeader()
            data.forEach {
                sender.sendData(it)
            }
            sender.sendFooter()
        }
        return false
    }
}
