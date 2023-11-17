package org.example

import kotlinx.coroutines.runBlocking
import org.example.client.Client
import org.example.server.Server

fun main() {
    runBlocking {
        if (joinedAsServer()) {
            val server = Server()
            server.start()
        } else {
            val client = Client(
                serverIP = "localhost",
                serverPort = Peer.SERVER_PORT
            )

//            val text = "are you there?"
//            client.send(TextEncoderDecoder().encode(text))
//            client.send(TextEncoderDecoder().encode("Hello there !"))
//            client.send(TextEncoderDecoder().encode("hello ভাই"))
            val imagePath = "C:\\Users\\Khalekuzzaman\\Desktop\\socket\\clinet\\img.jpg"
            client.send(imagePath)
            while (true) {

            }
        }
    }

}

fun joinedAsServer(): Boolean {
    var userInput: Char
    do {
        print("Enter 'c' for client or 's' for server: ")
        userInput = readlnOrNull()?.trim()?.lowercase()?.firstOrNull() ?: ' '
    } while (userInput != 'c' && userInput != 's')
    return userInput == 's'
}


