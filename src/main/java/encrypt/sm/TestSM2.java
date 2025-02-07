package encrypt.sm;

import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.*;
import org.bouncycastle.crypto.signers.SM2Signer;
import org.bouncycastle.util.encoders.Hex;

import java.security.SecureRandom;

/**
 * SM2加密解密示例
 * SM2是非对称加密算法，它使用一个特殊的椭圆曲线，称为SM2曲线，该曲线具有特殊的属性，使得SM2加密算法比其他加密算法更安全。
 */
public class TestSM2 {

    public static void main(String[] args) throws Exception {
        // 生成密钥对
        AsymmetricCipherKeyPair keyPair = generateKeyPair();
        ECPrivateKeyParameters privateKey = (ECPrivateKeyParameters) keyPair.getPrivate();
        System.out.println("Private Key: " + Hex.toHexString(privateKey.getD().toByteArray()));
        ECPublicKeyParameters publicKey = (ECPublicKeyParameters) keyPair.getPublic();
        System.out.println("Public Key: " + Hex.toHexString(publicKey.getQ().getEncoded(false)));

        publicEncryptTest(publicKey, privateKey);
        signTest(publicKey, privateKey);
    }

    private static void signTest(ECPublicKeyParameters publicKey, ECPrivateKeyParameters privateKey) {
        // 要加密的数据
        String plaintext = "Hello, World!";
        System.out.println("Plaintext: " + plaintext);
        byte[] data = plaintext.getBytes();
        try {
            byte[] signature = sign(privateKey, data);
            if (verify(publicKey, data, signature)) {
                System.out.println("Signature verification succeeded.");
            } else {
                System.out.println("Signature verification failed.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void publicEncryptTest(ECPublicKeyParameters publicKey, ECPrivateKeyParameters privateKey) {
        // 要加密的数据
        String plaintext = "Hello, World!";
        System.out.println("1-->Plaintext: " + plaintext);
        byte[] data = plaintext.getBytes();

        try {
            // 加密
            byte[] ciphertext = null;
            ciphertext = encryptWithPublicKey(publicKey, data);
            System.out.println("1-->Ciphertext: " + Hex.toHexString(ciphertext));

            // 解密
            byte[] decryptedData = decryptWithPrivateKey(privateKey, ciphertext);
            System.out.println("1-->Decrypted: " + new String(decryptedData));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("1-->failed: " + e.getMessage());
        }
    }

    /**
     * 生成密钥对
     */
    public static AsymmetricCipherKeyPair generateKeyPair() {
        // 使用国密SM2曲线
        X9ECParameters spec = CustomNamedCurves.getByName("sm2p256v1");
        ECDomainParameters domainParameters = new ECDomainParameters(
                spec.getCurve(), spec.getG(), spec.getN(), spec.getH());

        ECKeyGenerationParameters keyGenParams = new ECKeyGenerationParameters(domainParameters, new SecureRandom());
        ECKeyPairGenerator keyGen = new ECKeyPairGenerator();
        keyGen.init(keyGenParams);

        return keyGen.generateKeyPair();
    }

    /**
     * 公钥加密
     */
    public static byte[] encryptWithPublicKey(ECPublicKeyParameters publicKey, byte[] data) throws Exception {
        SM2Engine engine = new SM2Engine();
        engine.init(true, new ParametersWithRandom(publicKey, new SecureRandom()));

        return engine.processBlock(data, 0, data.length);
    }

    /**
     * 私钥解密
     */
    public static byte[] decryptWithPrivateKey(ECPrivateKeyParameters privateKey, byte[] ciphertext) throws Exception {
        SM2Engine engine = new SM2Engine();
        engine.init(false, privateKey);

        return engine.processBlock(ciphertext, 0, ciphertext.length);
    }

    /**
     * 签名
     */
    public static byte[] sign(ECPrivateKeyParameters privateKey, byte[] data) throws Exception {
        SM2Signer signer = new SM2Signer();
        signer.init(true, privateKey);
        signer.update(data, 0, data.length);
        return signer.generateSignature();
    }

    /**
     * 验签
     */
    public static boolean verify(ECPublicKeyParameters publicKey, byte[] data, byte[] signature) throws Exception {
        SM2Signer signer = new SM2Signer();
        signer.init(false, publicKey);
        signer.update(data, 0, data.length);
        return signer.verifySignature(signature);
    }
}

