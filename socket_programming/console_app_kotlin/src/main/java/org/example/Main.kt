package org.example

fun main() {

    val msg = "Hello ভাই"
    val byteArray = msg.toByteArray(Charsets.UTF_8)
    byteArray.forEach { print("$it ") }
    println()
    byteArray.forEach { print("${it.toInt() and 0xFF} ") }

}

class TextEncoderDecoder {

    fun encode(text: String): ByteArray {
        return text.toByteArray(Charsets.UTF_8)
    }

    fun decode(byteArray: ByteArray): String {
        return byteArray.toString(Charsets.UTF_8)
    }
}
