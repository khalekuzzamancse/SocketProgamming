package org.example

import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.io.InputStream
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.Charset


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
        val headerFooterSLength = 10
        var data = mutableListOf<Byte>()
        while (true) {
            try {
                val rec = ByteArray(20)
                val inputStream: InputStream = socket.getInputStream()
                val receivedLengthInBytes = inputStream.read(rec)
                val isDataReceived = receivedLengthInBytes != -1
                if (isDataReceived) {
                    val isEithereHeaderOrFooteer = receivedLengthInBytes == headerFooterSLength
                    if (isEithereHeaderOrFooteer) {
                        val info = String(rec, 0, receivedLengthInBytes, Charsets.UTF_8)
                        val isFooter = info.trimEnd() == "@END"
                        val isHeader = !isFooter
                        if (isHeader) {
                            data = mutableListOf()
                        } else if (isFooter) {
                            // val res= String(arr, 0, data.size, Charset.defaultCharset())
                            //  println(res)
                            println(data)
                            data = mutableListOf()

                        }
                    } else {//then it is data
                        val dataLengthInBytes = 1
                        val characterValue = String(rec, 0, dataLengthInBytes, Charsets.UTF_8)
                        print("$characterValue")
                        data.add(rec[0])

                    }

                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }


}


class ClientHandler(clientSocket: Socket) {

}