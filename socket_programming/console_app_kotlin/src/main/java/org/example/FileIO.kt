package org.example

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


fun main() {
    runBlocking {
        readingDemo()
        writingDemo()
        readingWritingImageDemo()
    }

}

suspend fun readingDemo() {
    val filePath = "C:\\Users\\Khalekuzzaman\\Desktop\\socket\\clinet\\img.jpg"
    fileRead(filePath, onReading = {
        println(it.size)
    }, onReadingFinished = {
        println("Finished")
    })
}

suspend fun readingWritingImageDemo() {
    val writer = PacketToFileWriter(
        directory = "C:\\Users\\Khalekuzzaman\\Desktop\\socket",
        fileName = "demo",
        extension = "jpg"
    )

    val readingPath = "C:\\Users\\Khalekuzzaman\\Desktop\\socket\\clinet\\img.jpg"
    fileRead(
        readingPath,
        onReading = {
            writer.write(it)
        },
        onReadingFinished = {
            writer.writeFinished()
            println("Writing Successfully")
        })
}


fun writingDemo() {
    val writer = PacketToFileWriter(
        directory = "C:\\Users\\Khalekuzzaman\\Desktop\\socket",
        fileName = "demo",
        extension = "txt"
    )
    writer.write("Hello, this is some sample data.".toByteArray())
    writer.write("This is Data Structure course.".toByteArray())
    writer.writeFinished()
}

suspend fun fileRead(
    path: String,
    onReading: suspend (bytes: ByteArray) -> Unit = {},
    onReadingFinished: suspend () -> Unit = {}
) {
    try {
        val fileInputStream = withContext(Dispatchers.IO) {
            FileInputStream(path)
        }
        val maxByteToRead = 1024
        val buffer = ByteArray(maxByteToRead)
        var readingNotFinished = true
        while (readingNotFinished) {
            val numberOfByteWasRead = withContext(Dispatchers.IO) {
                fileInputStream.read(buffer)
            }
            val noMoreByteToRead = numberOfByteWasRead <= 0
            if (noMoreByteToRead) {
                readingNotFinished = false
                withContext(Dispatchers.IO) {
                    fileInputStream.close()
                }
                onReadingFinished()
            } else {
                val actualReadBytes = buffer.copyOf(numberOfByteWasRead)
                onReading(actualReadBytes)
                //avoid sharing the same array that is why
                //returning a new array
//                println("Number of bytes read: $numberOfByteWasRead  : ${actualReadBytes.size}")
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}


class PacketToFileWriter(
    directory: String,
    fileName: String,
    extension: String
) {
    private val path = "$directory\\$fileName.$extension"
    private val outputStream = FileOutputStream(path)

    fun write(packet: ByteArray) {
        try {
            outputStream.write(packet)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun writeFinished() {
        try {
            outputStream.close()
            println("Bytes written to file: $path")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
