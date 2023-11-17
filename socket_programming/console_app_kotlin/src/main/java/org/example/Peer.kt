package org.example


interface Peer {
    companion object {
        const val SERVER_PORT = 43234
    }

    suspend fun send(data: ByteArray): Boolean
    fun observeReceiveData()
}

