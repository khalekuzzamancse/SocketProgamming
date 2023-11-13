package org.example

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.*

fun main() {
    runBlocking {
        print("Please enter 's' or 'c':")
        val userInput = readlnOrNull()
        if (userInput != null) {
            when (userInput.trim().lowercase(Locale.getDefault())) {
                "s" -> Server().start()
                "c" -> {
                    val myClient = Client("localhost")
                    myClient.connect()
                    delay(5_000)
                    val isNotSent =!myClient.send("Hi".toByteArray())
                 myClient.send("Hello".toByteArray())
                    if (isNotSent)
                        myClient.connect()
                }

                else -> println("Please enter 's' or 'c'.")
            }

        }
        while (true){

        }
    }

}
