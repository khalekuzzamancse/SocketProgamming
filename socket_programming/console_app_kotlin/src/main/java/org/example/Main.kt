package org.example

import kotlinx.coroutines.*


suspend fun doSomething(): Int {
    return withContext(Dispatchers.IO) {
        var sum = 0
        for (i in 1..3) {
            sum += i
            delay(1000)
        }
        sum//return
    }
}


fun main() {

    val scope = CoroutineScope(Dispatchers.IO)
    scope.launch {
        println(doSomething())
    }

    runBlocking {

    }
    println("MainScope:END")
}