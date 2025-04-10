package encrypt.dh;

import encrypt.EncryptUtil;

import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHParameterSpec;
import java.math.BigInteger;
import java.security.*;

public class DHUtil {
    /**
     * 素数 P
     */
    private static final BigInteger P = new BigInteger("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD1" +
            "29024E088A67CC74020BBEA63B139B22514A08798E3404DD" +
            "EF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245" +
            "E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7ED" +
            "EE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3D" +
            "C2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F" +
            "83655D23DCA3AD961C62F356208552BB9ED529077096966D" +
            "670C354E4ABC9804F1746C08CA18217C32905E462E36CE3B" +
            "E39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9" +
            "DE2BCBF6955817183995497CEA956AE515D2261898FA0510" +
            "15728E5A8AACAA68FFFFFFFFFFFFFFFF", 16);

    /**
     * 底数 G
     */
    private static final BigInteger G = new BigInteger("2", 16);

    /**
     * 生成DH密钥对
     */
    public static KeyPair generateKeyPair() {
        try {
            // 创建DH密钥对生成器
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
            // 指定素数 P 和底数 G 以及密钥长度
            keyPairGenerator.initialize(new DHParameterSpec(P, G, 2048));
            // 生成密钥对
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DH密钥交换
     */
    public static void exchange(KeyPair keyPairA, KeyPair keyPairB) {
        // 获取私钥
        PrivateKey aPrivate = keyPairA.getPrivate();
        // 获取公钥
        PublicKey bPublic = keyPairB.getPublic();
        // A使用B的公钥生成密钥
        byte[] bytesAB = exchangeInternal(aPrivate, bPublic);
        System.out.println("SecretKeyAB: " + EncryptUtil.bytesToHexString(bytesAB));

        PrivateKey bPrivate = keyPairB.getPrivate();
        PublicKey aPublic = keyPairA.getPublic();
        // B使用A的公钥生成密钥
        byte[] bytesBA = exchangeInternal(bPrivate, aPublic);
        System.out.println("SecretKeyBA: " + EncryptUtil.bytesToHexString(bytesBA));
    }

    /**
     * 协商密钥生成
     */
    public static byte[] exchangeInternal(PrivateKey privateKey, PublicKey publicKey) {
        try {
            // 创建密钥交换协议
            KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
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
