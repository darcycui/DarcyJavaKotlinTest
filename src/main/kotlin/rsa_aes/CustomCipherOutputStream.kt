package rsa_aes

import java.io.FilterOutputStream
import java.io.IOException
import java.io.OutputStream
import javax.crypto.*

/**
 * 自定义 CipherOutputStream
 * 唯一修改点： [close]时 不关闭底层输出流
 */
class CustomCipherOutputStream(
    outputStream: OutputStream,
    private val cipher: Cipher
) : FilterOutputStream(outputStream) {

    private val ibuffer = ByteArray(1)
    private var obuffer: ByteArray? = null
    private var closed = false

    private fun ensureCapacity(inLen: Int) {
        if (obuffer == null || obuffer!!.isEmpty()) {
            return
        }
        val minLen = cipher.getOutputSize(inLen)
        if (obuffer!!.size < minLen) {
            obuffer = ByteArray(minLen)
        }
    }

    override fun write(b: Int) {
        ibuffer[0] = b.toByte()
        ensureCapacity(1)
        try {
            val ostored: Int
            if (obuffer != null && obuffer!!.isNotEmpty()) {
                ostored = cipher.update(ibuffer, 0, 1, obuffer)
            } else {
                obuffer = cipher.update(ibuffer, 0, 1)
                ostored = obuffer?.size ?: 0
            }
            if (ostored > 0) {
                out.write(obuffer, 0, ostored)
            }
        } catch (sbe: ShortBufferException) {
            throw IOException(sbe)
        }
    }

    override fun write(b: ByteArray) {
        write(b, 0, b.size)
    }

    override fun write(b: ByteArray, off: Int, len: Int) {
        ensureCapacity(len)
        try {
            val ostored: Int
            if (obuffer != null && obuffer!!.isNotEmpty()) {
                ostored = cipher.update(b, off, len, obuffer)
            } else {
                obuffer = cipher.update(b, off, len)
                ostored = obuffer?.size ?: 0
            }
            if (ostored > 0) {
                out.write(obuffer, 0, ostored)
            }
        } catch (e: ShortBufferException) {
            throw IOException(e)
        }
    }

    override fun flush() {
        out.flush()
    }

    override fun close() {
        if (closed) return

        closed = true
        ensureCapacity(0)
        try {
            val ostored: Int
            if (obuffer != null && obuffer!!.isNotEmpty()) {
                ostored = cipher.doFinal(obuffer, 0)
            } else {
                obuffer = cipher.doFinal()
                ostored = obuffer?.size ?: 0
            }
            if (ostored > 0) {
                out.write(obuffer, 0, ostored)
            }
        } catch (e: IllegalBlockSizeException) {
        } catch (e: BadPaddingException) {
        } catch (e: ShortBufferException) {
        }
        obuffer = null
        try {
            flush()
        } catch (_: IOException) {
        }
        // 注意: 这里不关闭底层输出流
//        out.close()
    }
}