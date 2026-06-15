package rsa_aes

import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object RSAUtil {
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
}