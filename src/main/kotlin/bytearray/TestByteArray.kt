package bytearray

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    val key = "1234567890abcdef1234567890abcdef".toByteArray()
    println("key预期32字节: ${key.decodeToString()}")
    println("key=${key.contentToString()}")
    println("hexKey=${key.toHexString()}")
    val iv = "1234567890abcdef".toByteArray()
    println("iv预期16字节: ${iv.decodeToString()}")
    println("iv=${iv.contentToString()}")
    println("hexIv=${iv.toHexString()}")
}