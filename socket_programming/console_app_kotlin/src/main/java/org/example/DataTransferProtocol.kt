package org.example

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
        const val FILE_TYPE_BYTE_LENGTH = 5 //max 3 ASCII/ UTF-8 character
        const val END_FLAG_BYTE_LENGTH = 3 //max 3 ASCII/ UTF-8 c
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
        if (!isFileTypeReceived(bytes.size)) {
            return null
        }
        val decodedString = bytes.decodeToString()
        return decodedString.trimEnd()
    }

    fun deCodeAsTextMessage(bytes: ByteArray) = bytes.decodeToString()

}

class Sender(
    private val extension: FileExtension
) : Protocol() {

    fun encodedFileType() = toByteArray(makeAtMostFiveChars(extension.extension))
    fun encodeEndFlag()=toByteArray("$$$")
    fun encodeAsTextMessage(text:String)=text.toByteArray()
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

//Unit test is written
class DataTransferProtocol(
    private val dataType: FileExtension
) {
    companion object {
        const val FILE_TYPE_BYTE_LENGTH = 10
        const val END_FLAG_BYTE_LENGTH = 5
        const val DATA_BYTE_LENGTH = 1


    }

    fun isFileTypeReceived(receivedByteLength: Int) =
        receivedByteLength == FILE_TYPE_BYTE_LENGTH

    fun isEndFlagReceived(receivedByteLength: Int) =
        receivedByteLength == END_FLAG_BYTE_LENGTH

    fun isDataPortionReceived(receivedByteLength: Int) =
        receivedByteLength == DATA_BYTE_LENGTH

    fun encodedFileType() = toByteArray(makeAtMostFiveChars(dataType.extension))
    fun decodeFileType() = decodeAndRemoveTrailingSpaces(dataType.extension.toByteArray())
    private fun makeAtMostFiveChars(inputString: String): String {
        var resultString = inputString.take(5)
        resultString = resultString.padEnd(5)
        print(resultString)
        return resultString
    }

    private fun toByteArray(inputString: String): ByteArray {
        return inputString.toByteArray()
    }

    private fun decodeAndRemoveTrailingSpaces(byteArray: ByteArray): String {
        val decodedString = byteArray.decodeToString()
        return decodedString.trimEnd()
    }



}

