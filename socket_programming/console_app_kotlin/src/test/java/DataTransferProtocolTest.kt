import org.example.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class ReceiverProtocolTest {
    @Test
    fun testIsFileTypeReceived() {
        assertTrue(ReceiverProtocol().isFileTypeReceived(5))
        assertFalse(ReceiverProtocol().isFileTypeReceived(1))
    }

    @Test
    fun testIsEndFlagReceived() {
        assertTrue(ReceiverProtocol().isEndFlagReceived(3))
        assertFalse(ReceiverProtocol().isEndFlagReceived(2))
    }

    @Test
    fun testIsDataPortionReceived() {
        assertTrue(ReceiverProtocol().isDataPortionReceived(1))
        assertFalse(ReceiverProtocol().isDataPortionReceived(2))
    }

    @Test
    fun testDecodeFileTypeOrNull() {
        val receivedBytes = "txt  ".toByteArray()
        println(receivedBytes.size)
        assertEquals("txt", ReceiverProtocol().decodeFileTypeOrNull(receivedBytes))
    }

    @Test
    fun encodeAsTextMessage() {
        val msg = "Hi how are you?"
        val receivedBytes = msg.toByteArray()
        assertEquals(msg, ReceiverProtocol().deCodeAsTextMessage(receivedBytes))
        assertNotEquals("Hi how are ?", ReceiverProtocol().deCodeAsTextMessage(receivedBytes))
    }
}

class SenderTest {
    @Test
    fun encodedFileType() {
        val senderProtocol = Sender(FileExtensions.Text)
        val expected = "txt  ".toByteArray()
        assertArrayEquals(expected, senderProtocol.encodedFileType())
    }

    @Test
    fun encodeEndFlag() {
        val senderProtocol = Sender(FileExtensions.Text)
        val expected = "$$$".toByteArray()
        assertArrayEquals(expected, senderProtocol.encodeEndFlag())
    }

    @Test
    fun encodeAsTextMessage() {
        val message = "Hi how are you?"
        val senderProtocol = Sender(FileExtensions.Text)
        val expected = message.toByteArray()
        assertArrayEquals(expected, senderProtocol.encodeAsTextMessage(message))
        assertNotEquals("Hi how are", senderProtocol.encodeAsTextMessage(message))
    }
}

class SenderReceiverTest() {
    @Test
    fun fileExtensionSendReceivedTest() {
        //sender side
        val senderProtocol = Sender(FileExtensions.Text)
        val sentFileType = senderProtocol.encodedFileType()
        //receiver side
        val receiverProtocol = ReceiverProtocol()
        val receivedFileType = "txt"
        assertEquals(receivedFileType, receiverProtocol.decodeFileTypeOrNull(sentFileType))
    }
    @Test
    fun endFlagSendReceivedTest() {
        //sender side
        val senderProtocol = Sender(FileExtensions.Text)
        val sentEndFlag = senderProtocol.encodeEndFlag()
        //receiver side
        val receiverProtocol = ReceiverProtocol()
       assertTrue(receiverProtocol.isEndFlagReceived(sentEndFlag))
    }
    @Test
    fun sendReceivedTextMessageTest() {

        //sender side
        val senderProtocol = Sender(FileExtensions.Text)
        val sentByte = senderProtocol.encodeAsTextMessage("How are you?")
        //receiver side
        val receiverProtocol = ReceiverProtocol()
        val receivedMessage="How are you?"
       assertEquals(receivedMessage,receiverProtocol.deCodeAsTextMessage(sentByte))
    }

}

