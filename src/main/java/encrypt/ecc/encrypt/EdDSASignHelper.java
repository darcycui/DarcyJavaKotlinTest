package encrypt.ecc.encrypt;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class EdDSASignHelper {
    private static final String ALGORITHM_EdDSA = "Ed25519"; // 椭圆曲线算法 EdDSA
    private static final String ALGORITHM_SIGN = "Ed25519"; // 签名算法

    public static KeyPair generateKeyPairEdDSA() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_EdDSA);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] sign(byte[] data, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance(ALGORITHM_SIGN);
            signature.initSign(privateKey);
            signature.update(data);
            return signature.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verify(byte[] data, byte[] sign,PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance(ALGORITHM_SIGN);
            signature.initVerify(publicKey);
            signature.update(data);
            return  signature.verify(sign);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }
}
