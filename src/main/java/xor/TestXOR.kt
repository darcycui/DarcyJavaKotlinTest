package xor

fun main() {
//    val originalString = "abc"
    val originalString = "45678"
    val key = 0x5A6B7C // ��������������������ֵ
    val encryptedString = TestXOR.xorString(originalString, key)
    logD(message = "Encrypted String: $encryptedString")

    // ������������һ�ο��Խ��ܻ���
    val decryptedString = TestXOR.xorString(encryptedString, key)
    logD(message = "Decrypted String: $decryptedString")

    val encryptedString2 = TestXOR.xorString2(originalString, key)
    logD(message = "Encrypted String2: $encryptedString2")

    // ������������һ�ο��Խ��ܻ���
    val decryptedString2 = TestXOR.xorString2(encryptedString, key)
    logD(message = "Decrypted String2: $decryptedString2")
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