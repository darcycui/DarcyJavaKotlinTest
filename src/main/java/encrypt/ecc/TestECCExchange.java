package encrypt.ecc;

import encrypt.EncryptUtil;
import encrypt.ecc.exchange.ECCExchangeHelper;
import encrypt.ecc.kdf.ChainKey;
import encrypt.ecc.kdf.HKDF;
import encrypt.ecc.user.Alice;
import encrypt.ecc.user.Bob;
import kotlin.Pair;

import java.security.*;
import java.util.Arrays;

public class TestECCExchange {
    private static Alice alice;
    private static PrivateKey aliceIdentityPrivateKey;
    private static PublicKey aliceIdentityPublicKey;
    private static KeyPair aliceEphemeralKey;
    private static PrivateKey aliceEphemeralPrivateKey;
    private static PublicKey aliceEphemeralPublicKey;
    private static byte[] SK;
    private static Bob bob;
    private static PrivateKey bobIdentityPrivateKey;
    private static PublicKey bobIdentityPublicKey;
    private static PrivateKey bobSignedPrePrivateKey;
    private static PublicKey bobSignedPrePublicKey;
    private static KeyPair bobOneTimePreKeyPair;
    private static PublicKey bobOneTimePrePublicKey;
    private static PrivateKey bobOneTimePrePrivateKey;
    private static byte[] SK2;

    private static KeyPair bobEphemeralKey;
    private static PrivateKey bobEphemeralPrivateKey;
    private static PublicKey bobEphemeralPublicKey;


    public static void main(String[] args) throws NoSuchAlgorithmException {
        // 生成密钥
        generateKeys();

        // Alice X3DH 密钥交换
        SK = aliceX3DH();
        EncryptUtil.log("Alice计算共享密钥", SK);
        // Bob X3DH 密钥交换
        SK2 = bobX3DH();
        EncryptUtil.log("Bob计算共享密钥", SK2);
        if (SK != null && Arrays.equals(SK, SK2)) {
            System.out.println("Alice 和 Bob 的密钥相同");
        } else {
            System.out.println("Alice 和 Bob 的密钥不相同");
        }
        // 双棘轮(DH棘轮 KDF棘轮)
        doubleRatchet();
    }

    private static void generateKeys() {
        alice = new Alice();
        aliceIdentityPrivateKey = alice.getIdentityPrivateKey();
        aliceIdentityPublicKey = alice.getIdentityPublicKey();
        EncryptUtil.log("Alice 的身份私钥", aliceIdentityPrivateKey);
        EncryptUtil.log("Alice 的身份公钥", aliceIdentityPublicKey);
        aliceEphemeralKey = ECCExchangeHelper.generateKeyPair();
        aliceEphemeralPrivateKey = aliceEphemeralKey.getPrivate();
        aliceEphemeralPublicKey = aliceEphemeralKey.getPublic();
        EncryptUtil.log("Alice 的临时私钥", aliceEphemeralPrivateKey);
        EncryptUtil.log("Alice 的临时公钥", aliceEphemeralPublicKey);

        bob = new Bob();
        bobIdentityPrivateKey = bob.getIdentityPrivateKey();
        bobIdentityPublicKey = bob.getIdentityPublicKey();
        EncryptUtil.log("Bob 的身份私钥", bobIdentityPrivateKey);
        EncryptUtil.log("Bob 的身份公钥", bobIdentityPublicKey);
        bobSignedPrePrivateKey = bob.getSignedPreKeyPrivateKey();
        bobSignedPrePublicKey = bob.getSignedPreKeyPublicKey();
        EncryptUtil.log("Bob 的预签名私钥", bobSignedPrePrivateKey);
        EncryptUtil.log("Bob 的预签名公钥", bobSignedPrePublicKey);
        bobOneTimePreKeyPair = bob.getOneTimePreKeyPair("1");
        bobOneTimePrePublicKey = bobOneTimePreKeyPair.getPublic();
        bobOneTimePrePrivateKey = bobOneTimePreKeyPair.getPrivate();
        EncryptUtil.log("Bob 的一次性私钥", bobOneTimePrePrivateKey);
        EncryptUtil.log("Bob 的一次性公钥", bobOneTimePrePublicKey);
    }

    private static byte[] aliceX3DH() {
        byte[] dh1 = ECCExchangeHelper.getSharedSecret(aliceIdentityPrivateKey, bobSignedPrePublicKey);
        byte[] dh2 = ECCExchangeHelper.getSharedSecret(aliceEphemeralPrivateKey, bobIdentityPublicKey);
        byte[] dh3 = ECCExchangeHelper.getSharedSecret(aliceEphemeralPrivateKey, bobSignedPrePublicKey);
        byte[] dh4 = ECCExchangeHelper.getSharedSecret(aliceEphemeralPrivateKey, bobOneTimePrePublicKey);
        byte[] sharedSecret = EncryptUtil.appendArrays(dh1, dh2, dh3, dh4);
        HKDF hkdf = new HKDF();
        return hkdf.deriveSecrets(sharedSecret, new byte[32], "Info".getBytes(), 64);
    }

    private static byte[] bobX3DH() {
        byte[] dh11 = ECCExchangeHelper.getSharedSecret(bobSignedPrePrivateKey, aliceIdentityPublicKey);
        byte[] dh22 = ECCExchangeHelper.getSharedSecret(bobIdentityPrivateKey, aliceEphemeralPublicKey);
        byte[] dh33 = ECCExchangeHelper.getSharedSecret(bobSignedPrePrivateKey, aliceEphemeralPublicKey);
        byte[] dh44 = ECCExchangeHelper.getSharedSecret(bobOneTimePrePrivateKey, aliceEphemeralPublicKey);
        byte[] sharedSecret2 = EncryptUtil.appendArrays(dh11, dh22, dh33, dh44);
        HKDF hkdf = new HKDF();
        return hkdf.deriveSecrets(sharedSecret2, new byte[32], "Info".getBytes(), 64);
    }

    private static void doubleRatchet() {

        // 1.Alice 发送消息-1
        Pair<byte[], byte[]> pairAlice = EncryptUtil.splitArray64(SK, 32);
        // Alice Root密钥
        byte[] K1 = pairAlice.getFirst();
        // Alice 接收链密钥
        byte[] K2 = pairAlice.getSecond();
        // Alice DH棘轮步进一次
        aliceEphemeralKey = ECCExchangeHelper.generateKeyPair();
        aliceEphemeralPrivateKey = aliceEphemeralKey.getPrivate();
        aliceEphemeralPublicKey = aliceEphemeralKey.getPublic();
        byte[] dhAlice = ECCExchangeHelper.getSharedSecret(aliceEphemeralPrivateKey, bobSignedPrePublicKey);
        HKDF hkdfAlice = new HKDF();
        byte[] dhRatchetAlice = hkdfAlice.deriveSecrets(dhAlice, K1, "DHInfo".getBytes(), 64); // 盐:K1
        pairAlice = EncryptUtil.splitArray64(dhRatchetAlice, 32);
        // Alice Root密钥(新)
        byte[] K3 = pairAlice.getFirst();
        // Alice 发送链密钥
        byte[] K4 = pairAlice.getSecond();
        // Alice 对称棘轮(发送链)步进一次
        ChainKey senderChainAlice = new ChainKey(hkdfAlice, K4, 1);
        byte[] messageKeyAlice = senderChainAlice.getMessageKeys(); // 计算消息密钥
        EncryptUtil.log("Alice的消息密钥-1", messageKeyAlice);
        senderChainAlice = senderChainAlice.getNextChainKey(); // 计算下一个发送链密钥

        // 2.Bob 接收消息-1
        Pair<byte[], byte[]> pairBob = EncryptUtil.splitArray64(SK2, 32);
        // Bob Root密钥
        byte[] K11 = pairBob.getFirst();
        // Bob 发送链密钥
        byte[] K22 = pairBob.getSecond();
        // Bob DH棘轮同步
        byte[] dhBob = ECCExchangeHelper.getSharedSecret(bobSignedPrePrivateKey, aliceEphemeralPublicKey);
        HKDF hkdfBob = new HKDF();
        byte[] dhRatchetBob = hkdfBob.deriveSecrets(dhBob, K11, "DHInfo".getBytes(), 64); // 盐:K11
        pairBob = EncryptUtil.splitArray64(dhRatchetBob, 32);
        // Bob Root密钥(新)
        byte[] K33 = pairBob.getFirst();
        // Bob 接收链密钥
        byte[] K44 = pairBob.getSecond();
        // 对称棘轮(接收链)步进一次
        ChainKey receiverChainBob = new ChainKey(hkdfBob, K44, 1);
        byte[] messageKeyBob = receiverChainBob.getMessageKeys(); // 计算消息密钥
        EncryptUtil.log("Bob的消息密钥-1", messageKeyBob);
        if (Arrays.equals(messageKeyAlice, messageKeyBob)) {
            System.out.println("Alice 和 Bob 的密钥相同-1");
        } else {
            System.out.println("Alice 和 Bob 的密钥不相同-1");
        }
        receiverChainBob = receiverChainBob.getNextChainKey(); // 计算新的接收链密钥

        // 3. Bob 发送消息-2
        // Bob DH棘轮步进一次
        bobEphemeralKey = ECCExchangeHelper.generateKeyPair();
        bobEphemeralPrivateKey = bobEphemeralKey.getPrivate();
        bobEphemeralPublicKey = bobEphemeralKey.getPublic();
        dhBob = ECCExchangeHelper.getSharedSecret(bobEphemeralPrivateKey, aliceEphemeralPublicKey);
        dhRatchetBob = hkdfBob.deriveSecrets(dhBob, K33, "DHInfo".getBytes(), 64);
        pairBob = EncryptUtil.splitArray64(dhRatchetBob, 32);
        // Bob Root密钥(新)
        byte[] K55 = pairBob.getFirst();
        // Bob 发送链密钥(新)
        byte[] K66 = pairBob.getSecond();
        // 对称棘轮(发送链)步进一次
        ChainKey senderChainBob = new ChainKey(hkdfBob, K66, 1);
        messageKeyBob = senderChainBob.getMessageKeys(); // 计算消息密钥
        EncryptUtil.log("Bob的消息密钥-2", messageKeyBob);
        senderChainBob = senderChainBob.getNextChainKey(); // 计算新的发送链密钥

        // 4. Alice 接收消息-2
        // Alice DH棘轮同步
        dhAlice = ECCExchangeHelper.getSharedSecret(aliceEphemeralPrivateKey, bobEphemeralPublicKey);
        dhRatchetAlice = hkdfAlice.deriveSecrets(dhAlice, K3, "DHInfo".getBytes(), 64);
        pairAlice = EncryptUtil.splitArray64(dhRatchetAlice, 32);
        // Alice Root密钥(新)
        byte[] K5 = pairAlice.getFirst();
        // Alice 接收链密钥(新)
        byte[] K6 = pairAlice.getSecond();
        // 对称棘轮(接收链)步进一次
        ChainKey receiverChainAlice = new ChainKey(hkdfAlice, K6, 1);
        messageKeyAlice = receiverChainAlice.getMessageKeys(); // 计算消息密钥
        EncryptUtil.log("Alice的消息密钥-2", messageKeyAlice);
        if (Arrays.equals(messageKeyAlice, messageKeyBob)) {
            System.out.println("Alice 和 Bob 的密钥相同-2");
        } else {
            System.out.println("Alice 和 Bob 的密钥不相同-2");
        }
        receiverChainAlice = receiverChainAlice.getNextChainKey(); // 计算新的接收链密钥

        // 5. Alice 发送消息-3和4和5
        // Alice DH棘轮步进一次
        aliceEphemeralKey = ECCExchangeHelper.generateKeyPair();
        aliceEphemeralPrivateKey = aliceEphemeralKey.getPrivate();
        aliceEphemeralPublicKey = aliceEphemeralKey.getPublic();
        dhAlice = ECCExchangeHelper.getSharedSecret(aliceEphemeralPrivateKey, bobEphemeralPublicKey);
        dhRatchetAlice = hkdfAlice.deriveSecrets(dhAlice, K5, "DHInfo".getBytes(), 64);
        pairAlice = EncryptUtil.splitArray64(dhRatchetAlice, 32);
        // Alice Root密钥(新)
        byte[] K7 = pairAlice.getFirst();
        // Alice 发送链密钥(新)
        byte[] K8 = pairAlice.getSecond();
        // 对称棘轮(发送链)步进一次
        senderChainAlice = new ChainKey(hkdfAlice, K8, 1);
        messageKeyAlice = senderChainAlice.getMessageKeys(); // 计算消息密钥
        EncryptUtil.log("Alice的消息密钥-3", messageKeyAlice);
        senderChainAlice = senderChainAlice.getNextChainKey(); // 计算新的发送链密钥

        // Alice 继续发送消息-4
        // 对称棘轮(发送链)步进一次
        byte[] messageKeyAliceSecond = senderChainAlice.getMessageKeys(); // 计算消息密钥
        EncryptUtil.log("Alice的消息密钥-4", messageKeyAliceSecond);
        senderChainAlice = senderChainAlice.getNextChainKey(); // 计算新的发送链密钥

        // Alice 继续发送消息-5
        byte[] messageKeyAliceThird = senderChainAlice.getMessageKeys(); // 计算消息密钥
        EncryptUtil.log("Alice的消息密钥-5", messageKeyAliceThird);
        senderChainAlice = senderChainAlice.getNextChainKey(); // 计算新的发送链密钥

        // 6.Bob 接收消息-3和4和5
        // DH棘轮同步
        dhBob = ECCExchangeHelper.getSharedSecret(bobEphemeralPrivateKey, aliceEphemeralPublicKey);
        dhRatchetBob = hkdfBob.deriveSecrets(dhBob, K55, "DHInfo".getBytes(), 64);
        pairBob = EncryptUtil.splitArray64(dhRatchetBob, 32);
        // Bob Root密钥(新)
        byte[] K77 = pairBob.getFirst();
        // Bob 接收链密钥(新)
        byte[] K88 = pairBob.getSecond();
        // 对称棘轮(接收链)步进一次
        receiverChainBob = new ChainKey(hkdfBob, K88, 1);
        messageKeyBob = receiverChainBob.getMessageKeys(); // 计算消息密钥
        EncryptUtil.log("Bob的消息密钥-3", messageKeyBob);
        if (Arrays.equals(messageKeyAlice, messageKeyBob)) {
            System.out.println("Alice 和 Bob 的密钥相同-3");
        } else {
            System.out.println("Alice 和 Bob 的密钥不相同-3");
        }
        receiverChainBob = receiverChainBob.getNextChainKey(); // 计算新的接收链密钥

        // Bob 继续接收消息4
        // 对称棘轮(接收链)步进一次
        byte[] messageKeyBobSecond = receiverChainBob.getMessageKeys(); // 计算消息密钥
        EncryptUtil.log("Bob的消息密钥-4", messageKeyBobSecond);
        if (Arrays.equals(messageKeyAliceSecond, messageKeyBobSecond)) {
            System.out.println("Alice 和 Bob 的密钥相同-4");
        } else {
            System.out.println("Alice 和 Bob 的密钥不相同-4");
        }
        receiverChainBob = receiverChainBob.getNextChainKey(); // 计算新的接收链密钥

        // Bob 继续接收消息5
        byte[] messageKeyBobThird = receiverChainBob.getMessageKeys(); // 计算消息密钥
        EncryptUtil.log("Bob的消息密钥-5", messageKeyBobThird);
        if (Arrays.equals(messageKeyAliceThird, messageKeyBobThird)) {
            System.out.println("Alice 和 Bob 的密钥相同-5");
        } else {
            System.out.println("Alice 和 Bob 的密钥不相同-5");
        }
        receiverChainBob = receiverChainBob.getNextChainKey(); // 计算新的接收链密钥

    }

}
