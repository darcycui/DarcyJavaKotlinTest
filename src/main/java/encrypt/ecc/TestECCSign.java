package encrypt.ecc;

import encrypt.ecc.sign.ECDSASignHelper;
import encrypt.ecc.sign.EdDSASignHelper;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class TestECCSign {
    public static void main(String[] args) {
        String originalString = "Hello, World!";
        testECDSA(originalString);
        testEdDSA(originalString);
    }

    private static void testEdDSA(String originalString) {
        KeyPair keyPair = EdDSASignHelper.generateKeyPairEdDSA();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        byte[] sign = EdDSASignHelper.sign(originalString.getBytes(), privateKey);
        boolean verify = EdDSASignHelper.verify(originalString.getBytes(), sign, publicKey);
        System.out.println("EdDSA sign 验证:" + verify);
    }

    private static void testECDSA(String originalString) {
        KeyPair keyPair = ECDSASignHelper.generateKeyPairECDSA();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        byte[] sign = ECDSASignHelper.sign(originalString.getBytes(), privateKey);
        boolean verify = ECDSASignHelper.verify(originalString.getBytes(), sign, publicKey);
        System.out.println("ECDSA sign 验证:" + verify);
    }
}
