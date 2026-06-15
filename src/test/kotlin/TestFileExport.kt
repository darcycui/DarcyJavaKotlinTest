import rsa_aes.FileExportUtil
import rsa_aes.RSAUtil
import java.io.File
import java.io.FileInputStream
import kotlin.test.Test
import kotlin.test.assertEquals


class TestFileExport {

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `test-rsa-encrypt-aes-key`() {
        val message = "1234567890abcdef1234567890abcdef"
        println("message=${message}")
        val encryptedMessage = RSAUtil.encryptRSA(message.toByteArray())
        println("encryptedMessage: ${encryptedMessage.toHexString()}")
        val decryptedMessage = RSAUtil.decryptRSA(encryptedMessage)
        val decryptedMessageStr = decryptedMessage.decodeToString()
        println("decryptedMessage: $decryptedMessageStr")
        assertEquals(message, decryptedMessageStr, "RSA加密解密失败")

    }

    @Test
    fun `test-file-encrypt`() {
        val fileIn = File("C:\\Projects\\IdeaProjects\\KotlinTest\\src\\test\\kotlin\\rsa_aes\\in.jpg")
        val fileOut = File("C:\\Projects\\IdeaProjects\\KotlinTest\\src\\test\\kotlin\\rsa_aes\\encrypt.jpg")
        FileExportUtil.encryptFile(fileIn, fileOut)
    }

    @Test
    fun `test-file-decrypt`() {
        val fileIn = File("C:\\Projects\\IdeaProjects\\KotlinTest\\src\\test\\kotlin\\rsa_aes\\encrypt.jpg")
        val fileOut = File("C:\\Projects\\IdeaProjects\\KotlinTest\\src\\test\\kotlin\\rsa_aes\\decrypt.jpg")
        FileExportUtil.decryptFile(fileIn, fileOut)
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `test-file-encrypt-stream`() {
        val fileIn = File("C:\\Projects\\IdeaProjects\\KotlinTest\\src\\test\\kotlin\\rsa_aes\\in.jpg")
        val fileOut = File("C:\\Projects\\IdeaProjects\\KotlinTest\\src\\test\\kotlin\\rsa_aes\\encrypt-stream.jpg")
        if (fileOut.exists()) {
            fileOut.delete()
        }

        FileExportUtil.encryptFileStream(fileIn, fileOut)
    }

    @Test
    fun `test-file-decrypt-stream`() {
        val fileIn = File("C:\\Projects\\IdeaProjects\\KotlinTest\\src\\test\\kotlin\\rsa_aes\\encrypt-stream.jpg")
        val fileOut = File("C:\\Projects\\IdeaProjects\\KotlinTest\\src\\test\\kotlin\\rsa_aes\\decrypt-stream.jpg")
        if (fileOut.exists()) {
            fileOut.delete()
        }
        FileExportUtil.decryptFile(fileIn, fileOut)
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `test-write-and-read-bytes`() {

        val fileIn = File("C:\\Projects\\IdeaProjects\\KotlinTest\\src\\test\\kotlin\\rsa_aes\\in.jpg")
        val fileOut = File("C:\\Projects\\IdeaProjects\\KotlinTest\\src\\test\\kotlin\\rsa_aes\\encrypt-stream.jpg")
        if (fileOut.exists()) {
            fileOut.delete()
        }
        val aesKey = "1234567890abcdef1234567890abcdef".toByteArray()
        println("aesKey: $aesKey")
        val rsaAESKey = RSAUtil.encryptRSA(aesKey)
        fileOut.appendBytes(rsaAESKey)

        val readStream = FileInputStream(fileOut)
        val readArray = ByteArray(256)
        readStream.read(readArray)
        val realKey = RSAUtil.decryptRSA(readArray)


        val readStr = realKey.toHexString()
        println("readStr: $readStr")
        assertEquals(aesKey.toHexString(), readStr, "写入和读取的Key不一致")
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `test-decrypt-phone-file`() {
//        val fileIn = File("C:\\Projects\\IdeaProjects\\KotlinTest\\src\\test\\kotlin\\rsa_aes\\phone.jpg")
//        val fileOut = File("C:\\Projects\\IdeaProjects\\KotlinTest\\src\\test\\kotlin\\rsa_aes\\decrypt-phone.jpg")
//        val fileIn = File("C:\\Projects\\IdeaProjects\\KotlinTest\\src\\test\\kotlin\\rsa_aes\\phone2.jpg")
//        val fileOut = File("C:\\Projects\\IdeaProjects\\KotlinTest\\src\\test\\kotlin\\rsa_aes\\decrypt-phone2.jpg")
        val fileIn = File("C:\\Projects\\IdeaProjects\\KotlinTest\\src\\test\\kotlin\\rsa_aes\\phone3.jpg")
        val fileOut = File("C:\\Projects\\IdeaProjects\\KotlinTest\\src\\test\\kotlin\\rsa_aes\\decrypt-phone3.jpg")
        if (fileOut.exists()) {
            fileOut.delete()
        }
        FileExportUtil.decryptFile(fileIn, fileOut)

    }

}