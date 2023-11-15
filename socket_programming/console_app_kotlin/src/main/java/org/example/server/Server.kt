package org.example.server

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.example.Peer
import org.example.ReceivedDataDecoder
import org.example.ReceiverProtocol
import org.example.client.ClientCommunicationHandler
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

    companion object {
        const val TAG = "Server:: "
    }

    fun start() {

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

    override suspend fun send(data: ByteArray): Boolean {
        TODO("Not yet implemented")
    }


    override fun observeReceiveData() {
        runBlocking {
            while (true) {
                try {
                    val inputStream: InputStream = socket.getInputStream()
                    val receivedLengthInBytes = inputStream.readAllBytes()
                    println("${receivedLengthInBytes.toList()} ")
                } catch (e: IOException) {
                    println("$TAG observeReceiveData() causes exception")
                }
                delay(1000)
            }

        }


    }


}


class ClientHandler(clientSocket: Socket) {

}