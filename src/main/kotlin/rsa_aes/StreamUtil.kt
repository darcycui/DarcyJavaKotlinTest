package org.example.rsa_aes

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


object StreamUtil {
    @Throws(IOException::class)
    fun copy(`in`: InputStream, out: OutputStream, needCloseStreams: Boolean): Long {
        val buffer = ByteArray(8192)
        var read: Int
        var total: Long = 0

        while ((`in`.read(buffer).also { read = it }) != -1) {
            out.write(buffer, 0, read)
            out.flush()
            total += read.toLong()
        }
        out.flush()

        if (needCloseStreams) {
            `in`.close()
            out.close()
        }

        return total
    }
}