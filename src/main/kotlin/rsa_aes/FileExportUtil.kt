package rsa_aes

import exts.logD
import java.io.*
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.SecureRandom
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.min

object FileExportUtil {

    @OptIn(ExperimentalStdlibApi::class)
    fun encryptFileStream(fileIn: File, fileOut: File) {
        if (fileOut.exists()) {
            fileOut.delete()
        }
        // 生成AESKey
        val key = ByteArray(32)
        SecureRandom().nextBytes(key)
        logD(message = "写入AES密钥长度：${key.size} key=${key.toHexString()}")
        // 生成IV
        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)
        logD(message = "写入AES IV长度：${key.size} iv=${iv.toHexString()}")
        val encryptedAesKey = RSAUtil.encryptRSA(key)
        logD(message = "写入RSA加密的AES密钥长度：${encryptedAesKey.size}")
        logD(message = "写入RSA加密的AES密钥：${encryptedAesKey.toHexString()}")
        val digest = MessageDigest.getInstance("SHA-256")

        // 单个底层输出流，追加模式（true）
        FileOutputStream(fileOut, true).use { fos ->
            // 1. 写入明文头部
            fos.write(encryptedAesKey)
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv))
            logD(message = "原文件长度：${fileIn.length()}")
            FileInputStream(fileIn).use { input ->
                // 2. 通过 CipherOutputStream 写入加密数据
                CipherOutputStream(fos, cipher).use { cipherOut ->
                    val buffer = ByteArray(1024 * 1024)
                    var bytesRead: Int
                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        cipherOut.write(buffer, 0, bytesRead)
                        digest.update(buffer, 0, bytesRead)
                    }
                    cipherOut.flush()
                }
            }
        }

        // 3. 写入明文尾部
        val hash256: ByteArray = digest.digest()
        logD(message = "写入hash256:长度：${hash256.size} ${hash256.toHexString()}")
        FileOutputStream(fileOut, true).use { fos ->
            fos.write(iv)
            fos.write(hash256)
        }
        logD(message = "加密文件总长度：${fileOut.length()}")
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Deprecated("not secure")
    fun encryptFile(inputFile: File, fileOut: File) {
        if (fileOut.exists()) {
            fileOut.delete()
        }
        val key = generateKey()
        logD(message = "写入AES密钥长度：${key.size} key=${key.toHexString()}")
        val iv = generateIV()
        logD(message = "写入AES IV长度：${key.size} iv=${iv.toHexString()}")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val secretKey = SecretKeySpec(key, "AES")
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
        val digest = MessageDigest.getInstance("SHA-256")

        // 使用缓冲区分块读取文件
        val bufferSize = 1024 * 1024 // 例如，可以设置为1MB
        val buffer = ByteArray(bufferSize)

        // 读取文件并加密
        inputFile.inputStream().use { inputStream ->
            fileOut.outputStream().use { outputStream ->
                // 1.将RSA加密的AES密钥写入输出流
                val rsaKey = RSAUtil.encryptRSA(key)
                outputStream.write(rsaKey)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    val encryptedBytes = cipher.update(buffer, 0, bytesRead)
                    // 2.写入加密数据
                    outputStream.write(encryptedBytes)
                    digest.update(buffer, 0, bytesRead)
                }
                // 处理最后一块数据
                val finalBytes = cipher.doFinal()
                if (finalBytes != null) {
                    outputStream.write(finalBytes)
                }
                // 3.写入初始化向量
                outputStream.write(iv)
                // 4.写入校验Hash
                val hash256 = digest.digest()
                logD(message = "写入hash256:${hash256.toHexString()}")
                outputStream.write(hash256)
            }
        }
        val totalLen = fileOut.length()
        logD(message = "加密后文件长度：$totalLen")
    }


    /**
     * 解密由 encryptFile 方法加密的文件，并从 PEM 文件加载 RSA 私钥
     *
     * @param inputStream      加密文件的输入流
     * @param outputStream     解密后数据的输出流
     * @param rsaKeyLength     RSA 加密密钥的长度（字节），默认 256
     */
    @OptIn(ExperimentalStdlibApi::class)
//    @Deprecated("not secure")
    fun decryptFile(
        fileIn: File,
        fileOut: File,
        rsaKeyLength: Int = 256,
    ) {
        if (fileOut.exists()) {
            fileOut.delete()
        }
        val totalLen = fileIn.length()
        logD(message = "加密文件总长度：$totalLen")
        val ivLength = 16
        val hashLength = 32
        val expectedHash = ByteArray(hashLength)
        require(totalLen > ivLength + hashLength) { "File too small: $totalLen bytes, need > $ivLength bytes" }

        // hash256
        val digest = MessageDigest.getInstance("SHA-256")

        RandomAccessFile(fileIn, "r").use { raf ->
            fileOut.outputStream().use { outputStream ->
                // 读取开头 key
                val encryptKey = ByteArray(rsaKeyLength)
                raf.seek(0)
                raf.readFully(encryptKey)
                logD(message = "获取RAS加密的AES密钥长度：${encryptKey.size} encryptKey=${encryptKey.toHexString()}")
                val aesKey = RSAUtil.decryptRSA(encryptKey)
                logD(message = "AES密钥长度：${aesKey.size} aesKey=${aesKey.toHexString()}")
                // 读取末尾 hash
                raf.seek(totalLen - hashLength)
                raf.readFully(expectedHash)
                logD(message = "期望的hash: expectedHash=${expectedHash.toHexString()}")
                // 读取末尾 IV
                raf.seek(totalLen - hashLength - ivLength)
                val iv = ByteArray(ivLength)
                raf.readFully(iv)
                logD(message = "读取AES的iv长度：${iv.size} iv=${iv.toHexString()}")
                // AES 解密
                val aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                val secretKeySpec = SecretKeySpec(aesKey, "AES")
                val ivSpec = IvParameterSpec(iv)
                aesCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec)
                // 回到内容部分，循环读取
                raf.seek(rsaKeyLength.toLong())
                val bodyLen = totalLen - rsaKeyLength - hashLength - ivLength
                logD(message = "解密前文件大小：$bodyLen")
                val buffer = ByteArray(1024 * 1024) // 缓冲区大小可根据需要调整，如 1MB = 1024 * 1024
                var remaining = bodyLen

                while (remaining > 0) {
                    val toRead = min(buffer.size.toLong(), remaining).toInt()
                    val bytesRead = raf.read(buffer, 0, toRead)
                    if (bytesRead == -1) break

                    val decrypted = aesCipher.update(buffer, 0, bytesRead)
                    if (decrypted.isNotEmpty()) {
                        // 写入输出文件
                        outputStream.write(decrypted)
                        // 更新 hash256
                        digest.update(decrypted)
                    }
                    remaining -= bytesRead
                }
                // 4. 处理最后一块（含填充）
                val finalDecrypted = aesCipher.doFinal()
                if (finalDecrypted.isNotEmpty()) {
                    outputStream.write(finalDecrypted)
                    digest.update(finalDecrypted)
                }
                outputStream.flush()
            }
        }

        // 5. 完整性校验
        val actualHash = digest.digest()
        logD(message = "解密后文件大小: ${fileOut.length()}")
        logD(message = "预期哈希值: ${expectedHash.toHexString()}")
        logD(message = "实际哈希值: ${actualHash.toHexString()}")
        require(actualHash.contentEquals(expectedHash)) {
            "文件完整性校验失败：SHA256 不匹配"
        }
    }

    private fun generateKey(): ByteArray {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256)
        val secretKey = keyGenerator.generateKey()
        return secretKey.encoded
    }

    private fun generateIV(): ByteArray {
        val secureRandom = SecureRandom()
        val iv = ByteArray(16) // 16 bytes IV for AES
        secureRandom.nextBytes(iv)
        return iv
    }


    fun getFileSHA256(file: File): ByteArray {
        val fis = FileInputStream(file)
        return getFileSHA256(fis)
    }

    fun getFileSHA256(fis: InputStream): ByteArray {
        val md = MessageDigest.getInstance("SHA-256")
        val bufferSize = 1024 * 1024
        val buffer = ByteArray(bufferSize)
        fis.use {
            var bytesRead: Int
            while (fis.read(buffer).also { bytesRead = it } != -1) {
                md.update(buffer, 0, bytesRead)
            }
            return md.digest()
        }
    }

    fun saveToFile(inputStream: InputStream, file: File) {
        inputStream.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }
}
