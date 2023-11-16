package org.example.server

import org.example.Peer
import org.example.TextEncoderDecoder
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
                observeReceiveData()
            } catch (_: Exception) {

            }

        }

    }

    override suspend fun send(data: ByteArray): Boolean {
        TODO("Not yet implemented")
    }


    override fun observeReceiveData() {
        val closeSignal = -1
        var shouldRead=true
        val dataBytes= mutableListOf<Byte>()
        try {
            val inputStream: InputStream = socket.getInputStream()
            while (shouldRead) {
                val bytes = inputStream.read()
                if(bytes!=closeSignal)
                    dataBytes.add(bytes.toByte())
                if (bytes == closeSignal){
                    inputStream.close()
                    shouldRead=false
                }

            }
            println("${TextEncoderDecoder().decode(dataBytes.toByteArray())} ")

        } catch (e: IOException) {
            println("$TAG observeReceiveData() causes exception")
        }
    }


}


class ClientHandler