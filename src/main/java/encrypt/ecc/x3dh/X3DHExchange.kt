package encrypt.ecc.x3dh

import encrypt.EncryptUtil
import encrypt.ecc.exchange.ECCExchangeHelper
import encrypt.ecc.kdf.ChainKey
import encrypt.ecc.kdf.HKDF
import encrypt.ecc.user.Alice
import encrypt.ecc.user.Bob
import exts.logD
import java.security.KeyPair
import java.security.NoSuchAlgorithmException
import java.security.PrivateKey
import java.security.PublicKey

@Throws(NoSuchAlgorithmException::class)
fun main() {
    val x3dhExchange = X3DHExchange()
    x3dhExchange.test()
}

class X3DHExchange {
    private lateinit var alice: Alice
    private lateinit var aliceIdentityPrivateKey: PrivateKey
    private lateinit var aliceIdentityPublicKey: PublicKey
    private lateinit var aliceEphemeralKey: KeyPair
    private lateinit var aliceEphemeralPrivateKey: PrivateKey
    private lateinit var aliceEphemeralPublicKey: PublicKey
    private lateinit var SK: ByteArray
    private lateinit var bob: Bob
    private lateinit var bobIdentityPrivateKey: PrivateKey
    private lateinit var bobIdentityPublicKey: PublicKey
    private lateinit var bobSignedPrePrivateKey: PrivateKey
    private lateinit var bobSignedPrePublicKey: PublicKey
    private lateinit var bobOneTimePreKeyPair: KeyPair
    private lateinit var bobOneTimePrePublicKey: PublicKey
    private lateinit var bobOneTimePrePrivateKey: PrivateKey
    private lateinit var SK2: ByteArray

    private lateinit var bobEphemeralKey: KeyPair
    private lateinit var bobEphemeralPrivateKey: PrivateKey
    private lateinit var bobEphemeralPublicKey: PublicKey

    fun test() {
        // 生成密钥
        generateKeys()

        // Alice X3DH 密钥交换
        SK = aliceX3DH()
        EncryptUtil.log("Alice计算共享密钥", SK)
        // Bob X3DH 密钥交换
        SK2 = bobX3DH()
        EncryptUtil.log("Bob计算共享密钥", SK2)
        if (SK != null && SK.contentEquals(SK2)) {
            logD(message = "Alice 和 Bob 的密钥相同")
        } else {
            logD(message = "Alice 和 Bob 的密钥不相同")
        }
        // 双棘轮(DH棘轮 KDF棘轮)
        doubleRatchet()
    }

    private fun generateKeys() {
        alice = Alice()
        aliceIdentityPrivateKey = alice.identityPrivateKey
        aliceIdentityPublicKey = alice.identityPublicKey
        EncryptUtil.log("Alice 的身份私钥", aliceIdentityPrivateKey)
        EncryptUtil.log("Alice 的身份公钥", aliceIdentityPublicKey)
        aliceEphemeralKey = ECCExchangeHelper.generateKeyPair()
        aliceEphemeralPrivateKey = aliceEphemeralKey.getPrivate()
        aliceEphemeralPublicKey = aliceEphemeralKey.getPublic()
        EncryptUtil.log("Alice 的临时私钥", aliceEphemeralPrivateKey)
        EncryptUtil.log("Alice 的临时公钥", aliceEphemeralPublicKey)

        bob = Bob()
        bobIdentityPrivateKey = bob.identityPrivateKey
        bobIdentityPublicKey = bob.identityPublicKey
        EncryptUtil.log("Bob 的身份私钥", bobIdentityPrivateKey)
        EncryptUtil.log("Bob 的身份公钥", bobIdentityPublicKey)
        bobSignedPrePrivateKey = bob.signedPreKeyPrivateKey
        bobSignedPrePublicKey = bob.signedPreKeyPublicKey
        EncryptUtil.log("Bob 的预签名私钥", bobSignedPrePrivateKey)
        EncryptUtil.log("Bob 的预签名公钥", bobSignedPrePublicKey)
        bobOneTimePreKeyPair = bob.getOneTimePreKeyPair("1")
        bobOneTimePrePublicKey = bobOneTimePreKeyPair.public
        bobOneTimePrePrivateKey = bobOneTimePreKeyPair.private
        EncryptUtil.log("Bob 的一次性私钥", bobOneTimePrePrivateKey)
        EncryptUtil.log("Bob 的一次性公钥", bobOneTimePrePublicKey)
    }

    private fun aliceX3DH(): ByteArray {
        val dh1 = ECCExchangeHelper.getSharedSecret(aliceIdentityPrivateKey, bobSignedPrePublicKey)
        val dh2 = ECCExchangeHelper.getSharedSecret(aliceEphemeralPrivateKey, bobIdentityPublicKey)
        val dh3 = ECCExchangeHelper.getSharedSecret(aliceEphemeralPrivateKey, bobSignedPrePublicKey)
        val dh4 = ECCExchangeHelper.getSharedSecret(aliceEphemeralPrivateKey, bobOneTimePrePublicKey)
        val sharedSecret = EncryptUtil.appendArrays(dh1, dh2, dh3, dh4)
        val hkdf = HKDF()
        return hkdf.deriveSecrets(sharedSecret, ByteArray(32), "Info".toByteArray(), 64)
    }

    private fun bobX3DH(): ByteArray {
        val dh11 = ECCExchangeHelper.getSharedSecret(bobSignedPrePrivateKey, aliceIdentityPublicKey)
        val dh22 = ECCExchangeHelper.getSharedSecret(bobIdentityPrivateKey, aliceEphemeralPublicKey)
        val dh33 = ECCExchangeHelper.getSharedSecret(bobSignedPrePrivateKey, aliceEphemeralPublicKey)
        val dh44 = ECCExchangeHelper.getSharedSecret(bobOneTimePrePrivateKey, aliceEphemeralPublicKey)
        val sharedSecret2 = EncryptUtil.appendArrays(dh11, dh22, dh33, dh44)
        val hkdf = HKDF()
        return hkdf.deriveSecrets(sharedSecret2, ByteArray(32), "Info".toByteArray(), 64)
    }

    private fun doubleRatchet() {
        val messageKeysMap: MutableMap<Int, ByteArray> = HashMap()

        // 1.Alice 发送消息-1
        var pairAlice = EncryptUtil.splitArray64(SK, 32)
        // Alice Root密钥
        val K1 = pairAlice.first
        // Alice 接收链密钥
        val K2 = pairAlice.second
        // Alice DH棘轮步进一次
        aliceEphemeralKey = ECCExchangeHelper.generateKeyPair()
        aliceEphemeralPrivateKey = aliceEphemeralKey.private
        aliceEphemeralPublicKey = aliceEphemeralKey.public
        var dhAlice = ECCExchangeHelper.getSharedSecret(aliceEphemeralPrivateKey, bobSignedPrePublicKey)
        val hkdfAlice = HKDF()
        var dhRatchetAlice = hkdfAlice.deriveSecrets(dhAlice, K1, "DHInfo".toByteArray(), 64) // 盐:K1
        pairAlice = EncryptUtil.splitArray64(dhRatchetAlice, 32)
        // Alice Root密钥(新)
        val K3 = pairAlice.first
        // Alice 发送链密钥
        val K4 = pairAlice.second
        // Alice 对称棘轮(发送链)步进一次
        var senderChainAlice = ChainKey(hkdfAlice, K4, 0)
        var messageKeyAlice = senderChainAlice.messageKeys // 计算消息密钥
        EncryptUtil.log("Alice的消息密钥-1", messageKeyAlice)
        senderChainAlice = senderChainAlice.nextChainKey // 计算下一个发送链密钥

        // 2.Bob 接收消息-1
        var pairBob = EncryptUtil.splitArray64(SK2, 32)
        // Bob Root密钥
        val K11 = pairBob.first
        // Bob 发送链密钥
        val K22 = pairBob.second
        // Bob DH棘轮同步
        var dhBob = ECCExchangeHelper.getSharedSecret(bobSignedPrePrivateKey, aliceEphemeralPublicKey)
        val hkdfBob = HKDF()
        var dhRatchetBob = hkdfBob.deriveSecrets(dhBob, K11, "DHInfo".toByteArray(), 64) // 盐:K11
        pairBob = EncryptUtil.splitArray64(dhRatchetBob, 32)
        // Bob Root密钥(新)
        val K33 = pairBob.first
        // Bob 接收链密钥
        val K44 = pairBob.second
        // 对称棘轮(接收链)步进一次
        var receiverChainBob = ChainKey(hkdfBob, K44, 0)
        var messageKeyBob = receiverChainBob.messageKeys // 计算消息密钥
        EncryptUtil.log("Bob的消息密钥-1", messageKeyBob)
        if (messageKeyAlice.contentEquals(messageKeyBob)) {
            logD(message = "Alice 和 Bob 的密钥相同-1")
        } else {
            logD(message = "Alice 和 Bob 的密钥不相同-1")
        }
        receiverChainBob = receiverChainBob.nextChainKey // 计算新的接收链密钥

        // 3. Bob 发送消息-2
        // Bob DH棘轮步进一次
        bobEphemeralKey = ECCExchangeHelper.generateKeyPair()
        bobEphemeralPrivateKey = bobEphemeralKey.private
        bobEphemeralPublicKey = bobEphemeralKey.public
        dhBob = ECCExchangeHelper.getSharedSecret(bobEphemeralPrivateKey, aliceEphemeralPublicKey)
        dhRatchetBob = hkdfBob.deriveSecrets(dhBob, K33, "DHInfo".toByteArray(), 64)
        pairBob = EncryptUtil.splitArray64(dhRatchetBob, 32)
        // Bob Root密钥(新)
        val K55 = pairBob.first
        // Bob 发送链密钥(新)
        val K66 = pairBob.second
        // 对称棘轮(发送链)步进一次
        var senderChainBob = ChainKey(hkdfBob, K66, 0)
        messageKeyBob = senderChainBob.messageKeys // 计算消息密钥
        EncryptUtil.log("Bob的消息密钥-2", messageKeyBob)
        senderChainBob = senderChainBob.nextChainKey // 计算新的发送链密钥

        // 4. Alice 接收消息-2
        // Alice DH棘轮同步
        dhAlice = ECCExchangeHelper.getSharedSecret(aliceEphemeralPrivateKey, bobEphemeralPublicKey)
        dhRatchetAlice = hkdfAlice.deriveSecrets(dhAlice, K3, "DHInfo".toByteArray(), 64)
        pairAlice = EncryptUtil.splitArray64(dhRatchetAlice, 32)
        // Alice Root密钥(新)
        val K5 = pairAlice.first
        // Alice 接收链密钥(新)
        val K6 = pairAlice.second
        // 对称棘轮(接收链)步进一次
        var receiverChainAlice = ChainKey(hkdfAlice, K6, 0)
        messageKeyAlice = receiverChainAlice.messageKeys // 计算消息密钥
        EncryptUtil.log("Alice的消息密钥-2", messageKeyAlice)
        if (messageKeyAlice.contentEquals(messageKeyBob)) {
            logD(message = "Alice 和 Bob 的密钥相同-2")
        } else {
            logD(message = "Alice 和 Bob 的密钥不相同-2")
        }
        receiverChainAlice = receiverChainAlice.nextChainKey // 计算新的接收链密钥

        // 5. Alice 发送消息-3和4和5
        // Alice DH棘轮步进一次
        aliceEphemeralKey = ECCExchangeHelper.generateKeyPair()
        aliceEphemeralPrivateKey = aliceEphemeralKey.private
        aliceEphemeralPublicKey = aliceEphemeralKey.public
        dhAlice = ECCExchangeHelper.getSharedSecret(aliceEphemeralPrivateKey, bobEphemeralPublicKey)
        dhRatchetAlice = hkdfAlice.deriveSecrets(dhAlice, K5, "DHInfo".toByteArray(), 64)
        pairAlice = EncryptUtil.splitArray64(dhRatchetAlice, 32)
        // Alice Root密钥(新)
        val K7 = pairAlice.first
        // Alice 发送链密钥(新)
        val K8 = pairAlice.second
        // 对称棘轮(发送链)步进一次
        var messageIndexSend = 0
        senderChainAlice = ChainKey(hkdfAlice, K8, 0)
        messageKeyAlice = senderChainAlice.messageKeys // 计算消息密钥
        EncryptUtil.log("Alice的消息密钥-3", messageKeyAlice)
        senderChainAlice = senderChainAlice.nextChainKey // 计算新的发送链密钥

        // Alice 继续发送消息-4
        // 对称棘轮(发送链)步进一次
        messageIndexSend = 1
        val messageKeyAliceSecond = senderChainAlice.messageKeys // 计算消息密钥
        EncryptUtil.log("Alice的消息密钥-4", messageKeyAliceSecond)
        senderChainAlice = senderChainAlice.nextChainKey // 计算新的发送链密钥

        // Alice 继续发送消息-5
        messageIndexSend = 2
        val messageKeyAliceThird = senderChainAlice.messageKeys // 计算消息密钥
        EncryptUtil.log("Alice的消息密钥-5", messageKeyAliceThird)
        senderChainAlice = senderChainAlice.nextChainKey // 计算新的发送链密钥

        // 6.Bob 接收消息-3和4和5
        // DH棘轮同步
        dhBob = ECCExchangeHelper.getSharedSecret(bobEphemeralPrivateKey, aliceEphemeralPublicKey)
        dhRatchetBob = hkdfBob.deriveSecrets(dhBob, K55, "DHInfo".toByteArray(), 64)
        pairBob = EncryptUtil.splitArray64(dhRatchetBob, 32)
        // Bob Root密钥(新)
        val K77 = pairBob.first
        // Bob 接收链密钥(新)
        val K88 = pairBob.second
        // 对称棘轮(接收链)步进一次
        var messageIndexReceive = 0
        receiverChainBob = ChainKey(hkdfBob, K88, 0)
        messageKeyBob = receiverChainBob.messageKeys // 计算消息密钥
        EncryptUtil.log("Bob的消息密钥-3", messageKeyBob)
        if (messageKeyAlice.contentEquals(messageKeyBob)) {
            logD(message = "Alice 和 Bob 的密钥相同-3")
        } else {
            logD(message = "Alice 和 Bob 的密钥不相同-3")
        }
        receiverChainBob = receiverChainBob.nextChainKey // 计算新的接收链密钥
        // 如果消息乱序 先收到消息-5 就需要存储消息-3的密钥
        if (messageIndexSend > messageIndexReceive) {
            messageKeysMap[messageIndexReceive] = messageKeyBob
            messageIndexReceive++
        }

        // Bob 继续接收消息4
        // 对称棘轮(接收链)步进一次
        val messageKeyBobSecond = receiverChainBob.messageKeys // 计算消息密钥
        EncryptUtil.log("Bob的消息密钥-4", messageKeyBobSecond)
        if (messageKeyAliceSecond.contentEquals(messageKeyBobSecond)) {
            logD(message = "Alice 和 Bob 的密钥相同-4")
        } else {
            logD(message = "Alice 和 Bob 的密钥不相同-4")
        }
        receiverChainBob = receiverChainBob.nextChainKey // 计算新的接收链密钥
        // 如果消息乱序 先收到消息-5 就需要存储消息-4的密钥
        if (messageIndexSend > messageIndexReceive) {
            messageKeysMap[messageIndexReceive] = messageKeyBobSecond
            messageIndexReceive++
        }

        // Bob 继续接收消息5
        val messageKeyBobThird = receiverChainBob.messageKeys // 计算消息密钥
        EncryptUtil.log("Bob的消息密钥-5", messageKeyBobThird)
        if (messageKeyAliceThird.contentEquals(messageKeyBobThird)) {
            logD(message = "Alice 和 Bob 的密钥相同-5")
        } else {
            logD(message = "Alice 和 Bob 的密钥不相同-5")
        }
        receiverChainBob = receiverChainBob.nextChainKey // 计算新的接收链密钥
        // 收到消息-5 messageIndexSend==messageIndexReceive
        if (messageIndexSend > messageIndexReceive) { // 这段代码不会执行
            messageKeysMap[messageIndexReceive] = messageKeyBobThird
            messageIndexReceive++
        }
        logD(message = "存储的消息密钥:$messageKeysMap")
    }


}