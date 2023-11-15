package org.example.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.example.DataSender
import org.example.FileExtensions
import org.example.Peer
import java.io.DataOutputStream
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
        val socket = serverSocket
        if (socket == null) {
            println("Client:: send:Failed due to: socket is null")
            return false
        }
        return try {
            withContext(Dispatchers.IO) {
                val outputStream = DataOutputStream(socket.getOutputStream())
                outputStream.write(data)
                outputStream.close()
            }
            true
        } catch (e: IOException) {
            print("${DataSender.TAG} send() causes IOException")
            false

        }

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

    suspend fun send(socket: Socket, data: ByteArray): Boolean = withContext(Dispatchers.IO) {

        var res = true
        val sender = DataSender(
            type = FileExtensions.Text,
            socket = socket
        )
        sender.sendHeader()
//        data.forEachIndexed { index, singleByteData ->
//            val isNotSuccess =!sender.sendData(singleByteData)
//            res=isNotSuccess
//            if (isNotSuccess) {
//                println("$TAG send()->Failed at ${index}Th byte data")
//            }
//        }
//        sender.sendEndFlag()
//        println("$TAG send()->Sent: ${data.map { it }}")
        res
    }

}
