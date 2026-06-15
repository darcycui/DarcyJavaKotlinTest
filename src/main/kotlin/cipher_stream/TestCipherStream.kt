package cipher_stream

import java.io.File

fun main() {
    val text = "Hello World. Hello Tom. Hello Jerry."
    val key = "1234567890abcdef1234567890abcdef"
    val iv = "1234567890abcdef"
    val filePrefix = "C:\\Projects\\IdeaProjects\\KotlinTest\\src\\main\\kotlin\\cipher_stream\\"
    val fileOriginal = File("${filePrefix}original.txt")
    fileOriginal.writeText(text)

    val fileEncrypted = File("${filePrefix}encrypted.txt")
    val fileDecrypted = File("${filePrefix}decrypted.txt")

//    // 加密输出流 加密文件
//    println("加密输出流加密文件")
//    CipherStreamUtil.encryptByCipherOutStream(fileOriginal, fileEncrypted, key.toByteArray(), iv.toByteArray())
//    // 解密输入流 解密文件
//    println("解密输入流解密文件")
//    CipherStreamUtil.decryptByCipherInputStream(fileEncrypted, fileDecrypted, key.toByteArray(), iv.toByteArray())


    // 加密输出流 加密文件并写入hash
    println("加密输出流加密文件 并写入hash")
    CipherStreamUtil.encryptByCipherOutStreamWithHash(fileOriginal, fileEncrypted, key.toByteArray(), iv.toByteArray())
    // 解密输入流 解密文件并验证hash
    println("解密输入流解密文件 并验证hash")
    CipherStreamUtil.decryptByCipherInputStreamWithHash(fileEncrypted, fileDecrypted, key.toByteArray(), iv.toByteArray())
}