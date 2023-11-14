package org.example

sealed interface FileExtension {
    val extension: String
}

data object Text : FileExtension {
    override val extension: String
        get() = "txt"
}
data object Jpg : FileExtension {
    override val extension: String
        get() = "jpg"
}


abstract class DataTransferProtocol(
    private val dataType: FileExtension
) {
    companion object {
        private const val FILE_TYPE_BYTE_LENGTH = 10
        private const val END_FLAG_BYTE_LENGTH = 5
        private const val DATA_BYTE_LENGTH = 1


    }

    fun isFileTypeReceived(receivedByteLength: Int) =
        receivedByteLength == FILE_TYPE_BYTE_LENGTH

    fun isEndFlagReceived(receivedByteLength: Int) =
        receivedByteLength == END_FLAG_BYTE_LENGTH

    fun isDataPortionReceived(receivedByteLength: Int) =
        receivedByteLength == DATA_BYTE_LENGTH

    fun encodedFileType() = toByteArray(makeAtMostFiveChars(dataType.extension))
    fun decodeFileType(byteArray: ByteArray) = decodeAndRemoveTrailingSpaces(byteArray)
    private fun makeAtMostFiveChars(inputString: String): String {
        var resultString = inputString.take(5)
        resultString = resultString.padEnd(5)
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

