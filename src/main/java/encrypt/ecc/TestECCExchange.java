package encrypt.ecc;

import encrypt.EncryptUtil;
import encrypt.ecc.exchange.ECCExchangeHelper;
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
        log("Alice计算共享密钥", SK);
        // Bob X3DH 密钥交换
        SK2 = bobX3DH();
        log("Bob计算共享密钥", SK2);
        if (SK != null && Arrays.equals(SK, SK2)) {
            System.out.println("Alice 和 Bob 的密钥相同");
        } else {
            System.out.println("Alice 和 Bob 的密钥不相同");
        }
        // 双棘轮
        doubleRatchet();
    }

    private static void generateKeys() {
        alice = new Alice();
        aliceIdentityPrivateKey = alice.getIdentityPrivateKey();
        aliceIdentityPublicKey = alice.getIdentityPublicKey();
        log("Alice 的身份私钥", aliceIdentityPrivateKey);
        log("Alice 的身份公钥", aliceIdentityPublicKey);
        aliceEphemeralKey = ECCExchangeHelper.generateKeyPair();
        aliceEphemeralPrivateKey = aliceEphemeralKey.getPrivate();
        aliceEphemeralPublicKey = aliceEphemeralKey.getPublic();
        log("Alice 的临时私钥", aliceEphemeralPrivateKey);
        log("Alice 的临时公钥", aliceEphemeralPublicKey);

        bob = new Bob();
        bobIdentityPrivateKey = bob.getIdentityPrivateKey();
        bobIdentityPublicKey = bob.getIdentityPublicKey();
        log("Bob 的身份私钥", bobIdentityPrivateKey);
        log("Bob 的身份公钥", bobIdentityPublicKey);
        bobSignedPrePrivateKey = bob.getSignedPreKeyPrivateKey();
        bobSignedPrePublicKey = bob.getSignedPreKeyPublicKey();
        log("Bob 的预签名私钥", bobSignedPrePrivateKey);
        log("Bob 的预签名公钥", bobSignedPrePublicKey);
        bobOneTimePreKeyPair = bob.getOneTimePreKeyPair("1");
        bobOneTimePrePublicKey = bobOneTimePreKeyPair.getPublic();
        bobOneTimePrePrivateKey = bobOneTimePreKeyPair.getPrivate();
        log("Bob 的一次性私钥", bobOneTimePrePrivateKey);
        log("Bob 的一次性公钥", bobOneTimePrePublicKey);
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

        Pair<byte[], byte[]> pair = EncryptUtil.splitArray(SK, 32);
        // Alice Root密钥
        byte[] K1 = pair.getFirst();
        // Alice 接收密钥
        byte[] K2 = pair.getSecond();
        log("Alice的接收密钥-1", K2);
        // Alice 双棘轮前进一次
        byte[] dhx = ECCExchangeHelper.getSharedSecret(aliceEphemeralPrivateKey, bobSignedPrePublicKey);
        HKDF hkdf = new HKDF();
        byte[] SKX = hkdf.deriveSecrets(dhx, K1, "Info".getBytes(), 64); // 盐:K1
        pair = EncryptUtil.splitArray(SKX, 32);
        // Alice Root密钥(新)
        byte[] K3 = pair.getFirst();
        // Alice 发送密钥
        byte[] K4 = pair.getSecond();
        log("Alice的发送密钥-1", K4);

        Pair<byte[], byte[]> pair2 = EncryptUtil.splitArray(SK2, 32);
        // Bob Root密钥
        byte[] K11 = pair2.getFirst();
        // Bob 发送密钥
        byte[] K22 = pair2.getSecond();
        log("Bob的发送密钥-1", K22);
        // Bob 双棘轮前进一次
        byte[] dhx2 = ECCExchangeHelper.getSharedSecret(bobSignedPrePrivateKey, aliceEphemeralPublicKey);
        HKDF hkdf2 = new HKDF();
        byte[] SKX2 = hkdf2.deriveSecrets(dhx2, K11, "Info".getBytes(), 64); // 盐:K11
        pair2 = EncryptUtil.splitArray(SKX2, 32);
        // Bob Root密钥(新)
        byte[] K33 = pair2.getFirst();
        // Bob 接收密钥
        byte[] K44 = pair2.getSecond();
        log("Bob的接收密钥-1", K44);

        if (Arrays.equals(K4, K44)) {
            System.out.println("Alice 和 Bob 的密钥相同-1");
        } else {
            System.out.println("Alice 和 Bob 的密钥不相同-1");
        }

        // Bob 双棘轮前进一次
        bobEphemeralKey = ECCExchangeHelper.generateKeyPair();
        bobEphemeralPrivateKey = bobEphemeralKey.getPrivate();
        bobEphemeralPublicKey = bobEphemeralKey.getPublic();
        dhx2 = ECCExchangeHelper.getSharedSecret(bobEphemeralPrivateKey, aliceEphemeralPublicKey);
        SKX2 = hkdf2.deriveSecrets(dhx2, K33, "Info".getBytes(), 64);
        pair2 = EncryptUtil.splitArray(SKX2, 32);
        // Bob Root密钥(新)
        byte[] K55 = pair2.getFirst();
        // Bob 发送密钥(新
        byte[] K66 = pair2.getSecond();
        log("Bob的发送密钥-2", K66);

        // Alice 双棘轮前进一次
        dhx = ECCExchangeHelper.getSharedSecret(aliceEphemeralPrivateKey, bobEphemeralPublicKey);
        SKX = hkdf.deriveSecrets(dhx, K3, "Info".getBytes(), 64);
        pair = EncryptUtil.splitArray(SKX, 32);
        // Alice Root密钥(新)
        byte[] K5 = pair.getFirst();
        // Alice 接收密钥(新)
        byte[] K6 = pair.getSecond();
        log("Alice的接收密钥-2", K6);
        if (Arrays.equals(K6, K66)) {
            System.out.println("Alice 和 Bob 的密钥相同-2");
        } else {
            System.out.println("Alice 和 Bob 的密钥不相同-2");
        }

        // Alice 双棘轮前进一次
        aliceEphemeralKey = ECCExchangeHelper.generateKeyPair();
        aliceEphemeralPrivateKey = aliceEphemeralKey.getPrivate();
        aliceEphemeralPublicKey = aliceEphemeralKey.getPublic();
        dhx = ECCExchangeHelper.getSharedSecret(aliceEphemeralPrivateKey, bobEphemeralPublicKey);
        SKX = hkdf.deriveSecrets(dhx, K5, "Info".getBytes(), 64);
        pair = EncryptUtil.splitArray(SKX, 32);
        // Alice Root密钥(新)
        byte[] K7 = pair.getFirst();
        // Alice 发送密钥(新)
        byte[] K8 = pair.getSecond();
        log("Alice的发送密钥-3", K8);
        // Alice 连续发送消息 只有KDF棘轮前进一次
        SKX = hkdf.deriveSecrets(dhx, K7, "Info".getBytes(), 64);
        pair = EncryptUtil.splitArray(SKX, 32);
        // Alice Root密钥(新)
        byte[] K9 = pair.getFirst();
        // Alice 接收密钥(新)
        byte[] K10 = pair.getSecond();
        log("Alice的发送密钥-4", K10);

        // Bob 双棘轮前进一次
        dhx2 = ECCExchangeHelper.getSharedSecret(bobEphemeralPrivateKey, aliceEphemeralPublicKey);
        SKX2 = hkdf2.deriveSecrets(dhx2, K55, "Info".getBytes(), 64);
        pair2 = EncryptUtil.splitArray(SKX2, 32);
        // Bob Root密钥(新)
        byte[] K77 = pair2.getFirst();
        // Bob 接收密钥(新)
        byte[] K88 = pair2.getSecond();
        log("Bob的接收密钥-3", K88);
        if (Arrays.equals(K8, K88)) {
            System.out.println("Alice 和 Bob 的密钥相同-3");
        } else {
            System.out.println("Alice 和 Bob 的密钥不相同-3");
        }
        // 接收连续消息 只有KDF棘轮前进一次
        SKX2 = hkdf2.deriveSecrets(dhx2, K77, "Info".getBytes(), 64);
        pair2 = EncryptUtil.splitArray(SKX2, 32);
        // Bob Root密钥(新)
        byte[] K99 = pair2.getFirst();
        // Bob 发送密钥(新)
        byte[] K110 = pair2.getSecond();
        log("Bob的发送密钥-4", K110);
        if (Arrays.equals(K10, K110)) {
            System.out.println("Alice 和 Bob 的密钥相同-4");
        } else {
            System.out.println("Alice 和 Bob 的密钥不相同-4");
        }

//        // Bob 双棘轮前进一次
//        bobEphemeralKey = ECCExchangeHelper.generateKeyPair();
//        bobEphemeralPrivateKey = bobEphemeralKey.getPrivate();
//        bobEphemeralPublicKey = bobEphemeralKey.getPublic();
//        dhx2 = ECCExchangeHelper.getSharedSecret(bobEphemeralPrivateKey, aliceEphemeralPublicKey);
//        SKX2 = hkdf2.deriveSecrets(dhx2, K55, "Info".getBytes(), 64);
//        pair2 = EncryptUtil.splitArray(SKX2, 32);
//        // Bob Root密钥(新)
//        byte[] K77 = pair2.getFirst();
//        // Bob 接收密钥(新)
//        byte[] K88 = pair2.getSecond();
//        log("Bob的接收密钥-3", K88);
//        if (Arrays.equals(K8, K88)) {
//            System.out.println("Alice 和 Bob 的密钥相同-3");
//        } else {
//            System.out.println("Alice 和 Bob 的密钥不相同-3");
//        }


    }

    private static void log(String info, Key key) {
        String hexString = EncryptUtil.bytesToHexString(key.getEncoded());
        System.out.println(info + ": " + hexString);
    }

    private static void log(String info, byte[] bytes) {
        String hexString = EncryptUtil.bytesToHexString(bytes);
        System.out.println(info + ": " + hexString);
    }
}
