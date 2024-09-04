package xor

fun main() {
//    val originalString = "abc"
    val originalString = "45678"
    val key = 0x5A6B7C // 这是用于异或操作的整数值
    val encryptedString = TestXOR.xorString(originalString, key)
    println("Encrypted String: $encryptedString")

    // 反向操作再异或一次可以解密回来
    val decryptedString = TestXOR.xorString(encryptedString, key)
    println("Decrypted String: $decryptedString")

    val encryptedString2 = TestXOR.xorString2(originalString, key)
    println("Encrypted String2: $encryptedString2")

    // 反向操作再异或一次可以解密回来
    val decryptedString2 = TestXOR.xorString2(encryptedString, key)
    println("Decrypted String2: $decryptedString2")
}

object TestXOR {
    fun xorString(input: String, key: Int): String {
        val result = StringBuilder()
        for (char in input) {
            val xorChar = char.toInt() xor key
            result.append(xorChar.toChar())
        }
        return result.toString()
    }

    fun xorString2(input: String, key: Int): String {
        val result = StringBuilder()
        input.map { result.append((it.code xor key).toChar()) }
        return result.toString()
    }

}