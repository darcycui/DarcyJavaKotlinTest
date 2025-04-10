package encrypt.dh;

import encrypt.EncryptUtil;

import javax.crypto.KeyAgreement;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class ECDHUtil {
    /**
     * 素数 P 和基点 G 由椭圆曲线定义
     */
//    private static final String CURVE_NAME = "secp256r1";
    private static final String CURVE_NAME = "secp384r1";
//    private static final String CURVE_NAME = "secp521r1";
    /**
     * 生成ECDH密钥对
     */
    public static KeyPair generateKeyPair() {
        try {
            // 创建密钥生成器
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            // 指定椭圆曲线参数
            keyPairGenerator.initialize(new ECGenParameterSpec(CURVE_NAME));
            // 生成密钥对
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 密钥交换
     */
    public static void exchange(KeyPair keyPairA, KeyPair keyPairB) {
        // 获取私钥
        PrivateKey aPrivate = keyPairA.getPrivate();
        // 获取公钥
        PublicKey bPublic = keyPairB.getPublic();
        // A使用B的公钥生成密钥
        byte[] secretKeyAB = exchangeInternal(aPrivate, bPublic);
        System.out.println("SecretKeyAB: " + EncryptUtil.bytesToHexString(secretKeyAB));

        PrivateKey bPrivate = keyPairB.getPrivate();
        PublicKey aPublic = keyPairA.getPublic();
        // B使用A的公钥生成密钥
        byte[] secretKeyBA = exchangeInternal(bPrivate, aPublic);
        System.out.println("SecretKeyBA: " + EncryptUtil.bytesToHexString(secretKeyBA));
    }

    /**
     * 生成密钥
     */
    private static byte[] exchangeInternal(PrivateKey privateKey, PublicKey publicKey) {
        try {
            // 创建密钥交换协议
            KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH");
            // 使用私钥初始化协议
            keyAgreement.init(privateKey);
            // 使用公钥进行密钥交换
            keyAgreement.doPhase(publicKey, true);
            // 指定算法 生成密钥
            return keyAgreement.generateSecret();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
