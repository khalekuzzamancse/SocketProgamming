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

            //text file send demo,use the textWriterDemo() to server side
//            val textPath = "C:\\Users\\Khalekuzzaman\\Desktop\\socket\\clinet\\hi.txt"
//            client.send(textPath)

            //
           //image  file send demo,use the    imageWriterDemo() to server side
//            val imagePath ="C:\\Users\\Khalekuzzaman\\Desktop\\socket\\clinet\\img.jpg"
//            client.send(imagePath)
          //video  file send demo,use the   videoWriterDemo() to server side
            val videoPath ="C:\\Users\\Khalekuzzaman\\Desktop\\socket\\clinet\\video.mp4"
            client.send(videoPath)
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


