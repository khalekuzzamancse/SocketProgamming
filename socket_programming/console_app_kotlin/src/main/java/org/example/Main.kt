package org.example

import kotlinx.coroutines.*


suspend fun imageFetch(): String {
    for (i in 1L..<30_000_000_000) {
    }
    if(true)
    return "----\n----"
    else
        return "fail"
}

suspend fun fetchBio(): String {
    for (i in 1L..<15_000_000_000) {
    }
    if(true)
        return "Name:Mr Bean\nId:147509"
    else
        return "fail"

}


fun main() {
    CoroutineScope(Dispatchers.IO).launch {
        println(fetchBio())
    }
    CoroutineScope(Dispatchers.IO).launch {
        println(imageFetch())
    }
    doOtherTask()

}

fun doOtherTask() {
    var i: Long = 0
    var task = 1
    while (true) {
        if (i % 1_000_000_000 == 0L) {
            println("Task $task finished, by ${Thread.currentThread().name}")
            task++
        }
        i++
    }
}
