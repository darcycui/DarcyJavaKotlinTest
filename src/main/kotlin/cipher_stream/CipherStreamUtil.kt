package cipher_stream

import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object CipherStreamUtil {
    fun encryptByCipherOutStream(fileIn: File, fileOut: File, key: ByteArray, iv: ByteArray) {
        if (!fileIn.exists()) {
            throw Exception("File not exists")
        }
        if (fileOut.exists()) {
            fileOut.delete()
        }
        val buffer = ByteArray(8 * 1024)
        val aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val aesKey = SecretKeySpec(key, "AES")
        val aesIV = IvParameterSpec(iv)
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey, aesIV)
        fileIn.inputStream().use { ins ->
            fileOut.outputStream().use { outs ->
                CipherOutputStream(outs, aesCipher).use { cipherOut ->
                    var bytesRead = 0
                    while (ins.read(buffer).also { bytesRead = it } != -1) {
                        cipherOut.write(buffer, 0, bytesRead)
                    }
                    cipherOut.flush()
                }
            }
        }
    }

    fun decryptByCipherInputStream(fileIn: File, fileOut: File, key: ByteArray, iv: ByteArray) {
        if (!fileIn.exists()) {
            throw Exception("File not exists")
        }
        if (fileOut.exists()) {
            fileOut.delete()
        }
        val buffer = ByteArray(8 * 1024)
        val aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val aesKey = SecretKeySpec(key, "AES")
        val aesIV = IvParameterSpec(iv)
        aesCipher.init(Cipher.DECRYPT_MODE, aesKey, aesIV)
        fileIn.inputStream().use { ins ->
            CipherInputStream(ins, aesCipher).use { cipherIn ->
                fileOut.outputStream().use { outs ->
                    var bytesRead = 0
                    while (cipherIn.read(buffer).also { bytesRead = it } != -1) {
                        outs.write(buffer, 0, bytesRead)
                    }
                }
            }
        }
    }


    /**
     * 加密 并在文件末尾写入 hash值（32字节）
     */
    @OptIn(ExperimentalStdlibApi::class)
    fun encryptByCipherOutStreamWithHash(
        fileIn: File,
        fileOut: File,
        key: ByteArray,
        iv: ByteArray,
    ) {
        println("开始加密并写入hash")
        if (!fileIn.exists()) {
            throw Exception("File not exists")
        }
        if (fileOut.exists()) {
            fileOut.delete()
        }
        // 初始化 AES cipher
        val aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val aesKey = SecretKeySpec(key, "AES")
        val aesIV = IvParameterSpec(iv)
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey, aesIV)
        // 初始化 hash digest
        val digest = MessageDigest.getInstance("SHA-256")

        // 读取文件
        val buffer = ByteArray(8 * 1024)
        fileIn.inputStream().use { ins ->
            fileOut.outputStream().use { outs ->
                CipherOutputStream(outs, aesCipher).use { cipherOut ->
                    var bytesRead = 0
                    while (ins.read(buffer).also { bytesRead = it } != -1) {
                        // 加密
                        cipherOut.write(buffer, 0, bytesRead)
                        // 计算hash
                        digest.update(buffer, 0, bytesRead)
                    }
                    cipherOut.flush()
                }
            }
        }
        // 写入hash
        val hash = digest.digest()
        println("写入hash: ${hash.toHexString()}")
        // 追加写入文件
        FileOutputStream(fileOut, true).use { fos ->
            fos.write(hash)
        }
    }

    /**
     * 解密 验证文件末尾的 hash值（32字节）
     */

    @OptIn(ExperimentalStdlibApi::class)
    fun decryptByCipherInputStreamWithHash(
        fileIn: File,
        fileOut: File,
        key: ByteArray,
        iv: ByteArray,
    ) {
        println("开始解密并验证hash")
        if (!fileIn.exists()) {
            throw Exception("File not exists")
        }
        if (fileOut.exists()) {
            fileOut.delete()
        }
        // 初始化 AES cipher
        val aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val aesKey = SecretKeySpec(key, "AES")
        val aesIV = IvParameterSpec(iv)
        aesCipher.init(Cipher.DECRYPT_MODE, aesKey, aesIV)
        // 初始化 hash digest
        val digest = MessageDigest.getInstance("SHA-256")

        // 读取文件
        val buffer = ByteArray(8 * 1024)
        fileIn.inputStream().use { ins ->
            val totalLength = fileIn.length()
            val hashLength = 32
            val bodyLength = totalLength - hashLength

            // RandomAccessFile 指定位置读
            RandomAccessFile(fileIn, "r").use { raf ->
                // 读取 末尾的hash
                val expectedHash = ByteArray(hashLength)
                raf.seek(bodyLength)
                raf.read(expectedHash)
                println("expectedHash: ${expectedHash.toHexString()}")

                // 从头读取文件
                raf.seek(0)
                fileOut.outputStream().use { outs ->
                    var remaining = bodyLength
                    while (remaining > 0) {
                        val bytesToRead = buffer.size.toLong().coerceAtMost(remaining)
                        val bytesRead = raf.read(buffer, 0, bytesToRead.toInt())
                        if (bytesRead == -1) break
                        // 解密
                        val decrypted = aesCipher.update(buffer, 0, bytesRead)
                        if (decrypted != null) {
                            // 写入文件
                            outs.write(decrypted)
                            // 计算hash
                            digest.update(decrypted)
                        }
                        remaining -= bytesRead
                    }
                    // 处理最后一块（含填充）
                    val finalDecrypted = aesCipher.doFinal()
                    if (finalDecrypted != null) {
                        outs.write(finalDecrypted)
                        digest.update(finalDecrypted)
                    }
                }
                // 验证hash
                val actualHash = digest.digest()
                println("actualHash: ${actualHash.toHexString()}")
                if (!actualHash.contentEquals(expectedHash)) {
                    throw Exception("Hash verification failed")
                } else {
                    println("Hash verification passed")
                }
            }
        }
    }
}