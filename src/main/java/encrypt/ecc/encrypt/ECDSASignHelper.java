package encrypt.ecc.encrypt;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class ECDSASignHelper {
    private static final String ALGORITHM_ECDSA = "EC"; // 椭圆曲线算法
    private static final String CURVE_NAME = "secp256r1"; // 指定椭圆曲线
    private static final String ALGORITHM_SIGN = "SHA256withECDSA"; // 签名算法

    public static KeyPair generateKeyPairECDSA() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_ECDSA);
            ECGenParameterSpec ecSpec = new ECGenParameterSpec(CURVE_NAME);
            keyPairGenerator.initialize(ecSpec, new SecureRandom());
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
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
