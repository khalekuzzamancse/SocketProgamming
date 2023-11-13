package org.example

import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.io.InputStream
import java.net.ServerSocket
import java.net.Socket


class Server(
    private val port: Int = Peer.SERVER_PORT
) : Peer {
    private val serverSocket = ServerSocket(port)
    private val connectedClients = mutableListOf<ClientHandler>()
    private var socket = Socket()

    fun start() {
        runBlocking {
            while (true) {
                try {
                    println("Server:running")
                    val clientSocket = serverSocket.accept()
                    socket = clientSocket
                    connectedClients.add(ClientHandler(clientSocket))
                    observeReceiveData()


                } catch (_: Exception) {
                }

            }
        }
    }

    fun stop() {

    }

    override suspend fun send(data: ByteArray): Boolean {
        TODO("Not yet implemented")
    }

    override fun observeReceiveData() {
        while (true) {
            try {
                val inputStream: InputStream = socket.getInputStream()
                val byteValue = inputStream.read()
                if (byteValue != -1) {
                    val characterValue = byteValue.toChar()
                    print("$characterValue")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }


}

class ClientHandler(clientSocket: Socket) {

}