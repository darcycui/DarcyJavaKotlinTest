package rsa_aes

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

    @OptIn(ExperimentalEncodingApi::class, ExperimentalStdlibApi::class)
    fun encryptRSA(data: ByteArray): ByteArray {
        println("原文长度:${data.size}")
        println("originalBytes=${data.toHexString()}")
//        val inputStream = FileInputStream("C:\\Projects\\IdeaProjects\\KotlinTest\\src\\test\\kotlin\\rsa_aes\\public_key.pem")
        val inputStream =
            FileInputStream("C:\\Projects\\IdeaProjects\\KotlinTest\\src\\test\\kotlin\\rsa_aes\\RSAPublic.pem")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val publicKeyPEM = buildString {
            reader.useLines { lines ->
                lines.filter { !it.startsWith("-----BEGIN") && !it.startsWith("-----END") }
                    .forEach { append(it) }
            }
        }
//        println("publicKeyPEM=$publicKeyPEM")
        val publicKeyDER = Base64.decode(publicKeyPEM.toByteArray(), 0)
        val keySpec = X509EncodedKeySpec(publicKeyDER)
        val keyFactory = KeyFactory.getInstance("RSA")
        val publicKey = keyFactory.generatePublic(keySpec)
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        return cipher.doFinal(data).also {
            println("加密后长度: ${it.size}")
            println("encryptedBytes=${it.toHexString()}")
//            val decryptedBytes = decryptRSA(it)
        }
    }

    @OptIn(ExperimentalEncodingApi::class, ExperimentalStdlibApi::class)
    fun decryptRSA(encryptedData: ByteArray): ByteArray {
        // 1. 从资源文件读取私钥 PEM 内容（过滤掉头尾标记）
//        val inputStream = FileInputStream("C:\\Projects\\IdeaProjects\\KotlinTest\\src\\test\\kotlin\\rsa_aes\\private_key.pem")
        val inputStream =
            FileInputStream("C:\\Projects\\IdeaProjects\\KotlinTest\\src\\test\\kotlin\\rsa_aes\\RSAPrivate.pem")

        val reader = BufferedReader(InputStreamReader(inputStream))
        val privateKeyPEM = buildString {
            reader.useLines { lines ->
                lines.filter { !it.startsWith("-----BEGIN") && !it.startsWith("-----END") }
                    .forEach { append(it) }
            }
        }
//        println("privateKeyPEM=$privateKeyPEM")

        // 2. Base64 解码得到 DER 字节
        val privateKeyDER = Base64.decode(privateKeyPEM.toByteArray(), 0)

        // 3. 生成私钥对象（使用 PKCS#8 格式）
        val keySpec = PKCS8EncodedKeySpec(privateKeyDER)
        val keyFactory = KeyFactory.getInstance("RSA")
        val privateKey = keyFactory.generatePrivate(keySpec)

        // 4. 解密（与加密算法保持一致）
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        println("解密前长度: ${encryptedData.size}")
        return cipher.doFinal(encryptedData).also {
            println("解密后长度: ${it.size}")
            println("decryptedBytes=${it.toHexString()}")
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun encryptFileStream(fileIn: File, fileOut: File) {
        // 生成AESKey
        val key = ByteArray(32)
        SecureRandom().nextBytes(key)
        println("写入AES密钥长度：${key.size} key=${key.toHexString()}")
        // 生成IV
        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)
        println("写入AES IV长度：${key.size} iv=${iv.toHexString()}")
        val encryptedAesKey = encryptRSA(key)
        println("写入RSA加密的AES密钥长度：${encryptedAesKey.size}")
        println("写入RSA加密的AES密钥：${encryptedAesKey.toHexString()}")
        val digest = MessageDigest.getInstance("SHA-256")

        // 单个底层输出流，追加模式（true）
        FileOutputStream(fileOut, true).use { fos ->
            // 1. 写入明文头部
            fos.write(encryptedAesKey)
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv))
            println("原文件长度：${fileIn.length()}")
            val cipherOutputStream = CipherOutputStream(fos, cipher)
            FileInputStream(fileIn).use { input ->
                // 2. 通过 CipherOutputStream 写入加密数据
                cipherOutputStream.let { cipherOut ->
                    val buffer = ByteArray(1024 * 1024)
                    var bytesRead: Int
                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        cipherOut.write(buffer, 0, bytesRead)
                        digest.update(buffer, 0, bytesRead)
                    }
                    cipherOut.flush()
                }
            }
            // 3. 写入明文尾部
            val hash256: ByteArray = digest.digest()
            fos.write(iv)
            fos.write(hash256)
            cipherOutputStream.close()
        }
        println("加密文件总长度：${fileOut.length()}")
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Deprecated("not secure")
    fun encryptFile(inputFile: File, fileOut: File) {
        val key = generateKey()
        println("写入AES密钥长度：${key.size} key=${key.toHexString()}")
        val iv = generateIV()
        println("写入AES IV长度：${key.size} iv=${iv.toHexString()}")
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
                val rsaKey = encryptRSA(key)
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
                println("写入hash256:${hash256.toHexString()}")
                outputStream.write(hash256)
            }
        }
        val totalLen = fileOut.length()
        println("加密后文件长度：$totalLen")
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
        val totalLen = fileIn.length()
        println("加密文件总长度：$totalLen")
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
                println("获取RAS加密的AES密钥长度：${encryptKey.size} encryptKey=${encryptKey.toHexString()}")
                val aesKey = decryptRSA(encryptKey)
                println("AES密钥长度：${aesKey.size} aesKey=${aesKey.toHexString()}")
                // 读取末尾 hash
                raf.seek(totalLen - hashLength)
                raf.readFully(expectedHash)
                println("期望的hash: expectedHash=${expectedHash.toHexString()}")
                // 读取末尾 IV
                raf.seek(totalLen - hashLength - ivLength)
                val iv = ByteArray(ivLength)
                raf.readFully(iv)
                println("读取AES的iv长度：${iv.size} iv=${iv.toHexString()}")
                // AES 解密
                val aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                val secretKeySpec = SecretKeySpec(aesKey, "AES")
                val ivSpec = IvParameterSpec(iv)
                aesCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec)
                // 回到内容部分，循环读取
                raf.seek(rsaKeyLength.toLong())
                val bodyLen = totalLen - rsaKeyLength - hashLength - ivLength
                println("解密前文件大小：$bodyLen")
                val buffer = ByteArray(1024 * 1024) // 缓冲区大小可根据需要调整，如 1MB = 1024 * 1024
                var remaining = bodyLen

                while (remaining > 0) {
                    val toRead = min(buffer.size.toLong(), remaining).toInt()
                    val bytesRead = raf.read(buffer, 0, toRead)
                    if (bytesRead == -1) break // 理论上不会发生，但安全处理

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
        println("解密后文件大小: ${fileOut.length()}")
        println("预期哈希值: ${expectedHash.toHexString()}")
        println("实际哈希值: ${actualHash.toHexString()}")
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
}
