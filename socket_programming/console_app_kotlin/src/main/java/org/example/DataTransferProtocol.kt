package org.example

import java.lang.StringBuilder

sealed interface FileExtension {
    val extension: String
}

object FileExtensions {
    data object Text : FileExtension {
        override val extension: String
            get() = "txt"
    }

    data object Jpg : FileExtension {
        override val extension: String
            get() = "jpg"
    }
}

abstract class Protocol {
    companion object {
        const val FILE_TYPE_BYTE_LENGTH = 20 //max 3 ASCII/ UTF-8 character
        const val END_FLAG_BYTE_LENGTH = 15//max 3 ASCII/ UTF-8 c
        const val DATA_BYTE_LENGTH = 1
    }
}

class ReceiverProtocol : Protocol() {
    fun isFileTypeReceived(receivedByteLength: Int) =
        receivedByteLength == FILE_TYPE_BYTE_LENGTH

    fun isEndFlagReceived(receivedByteLength: Int) =
        receivedByteLength == END_FLAG_BYTE_LENGTH

    fun isEndFlagReceived(bytes: ByteArray) =
        bytes.contentEquals("$$$".toByteArray())

    fun isDataPortionReceived(receivedByteLength: Int) =
        receivedByteLength == DATA_BYTE_LENGTH

    fun decodeFileTypeOrNull(bytes: ByteArray): String? {
        //since taking array so it  mutable and reference is sharing
        //to avoid unwanted behaviour first copy it then do ..
        return if (bytes.size >= FILE_TYPE_BYTE_LENGTH) {
            val decodedString = bytes.copyOf().decodeToString()
            return decodedString.filter { it in 'a'..'z' }
        } else {
            null
        }
    }

    fun deCodeAsTextMessage(bytes: ByteArray) = bytes.decodeToString()

}

class Sender(
    private val extension: FileExtension
) : Protocol() {

    fun encodedFileType() = toByteArray(makeAtMostFiveChars(extension.extension))
    fun encodeEndFlag() = toByteArray("$$$")
    fun encodeAsTextMessage(text: String) = text.toByteArray()
    private fun makeAtMostFiveChars(inputString: String): String {
        var resultString = inputString.take(5)
        resultString = resultString.padEnd(5)
        print(resultString)
        return resultString
    }

    private fun toByteArray(inputString: String): ByteArray {
        return inputString.toByteArray()
    }

}

//since taking array so it  mutable and reference is sharing
//to avoid unwanted behaviour first copy it then do ..
class ReceivedDataDecoder {
    private val protocol = ReceiverProtocol()
    private var type: String? = null
    private var endFlag = ""
    private val data = mutableListOf<Byte>()
    fun onFileTypeReceived(bytes: ByteArray) {
        type = protocol.decodeFileTypeOrNull(bytes.copyOf())
    }

    fun onEndFlagReceived(bytes: ByteArray) {
        endFlag = "$$$"
    }

    fun onDataByteReceived(byte: Byte) {
        data.add(byte)
    }

    fun decodeAsText() {
        println("------")
//        println("FileType:$type")
//        println("Data:\n${org.example.TextEncoderDecoder().decode(data.toByteArray())}")
        println(data.toString())
        println("------")
    }

}



