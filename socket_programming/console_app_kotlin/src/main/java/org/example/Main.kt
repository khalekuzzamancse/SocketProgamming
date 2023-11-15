package org.example

fun main() {
    val send = "are you there ?"
    val bytes = TextEncoderDecoder().encode(send)
    println(bytes.toList())
    val rec = TextEncoderDecoder().decode(bytes)
    println(rec)
    println(TextEncoderDecoder().encode(rec).toList())
}

class TextEncoderDecoder {

    fun encode(text: String): ByteArray {
        return text.toByteArray(Charsets.UTF_8)
    }

    fun decode(byteArray: ByteArray): String {
        return byteArray.toString(Charsets.UTF_8)
    }
}
