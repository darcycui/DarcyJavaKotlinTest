package encrypt.rsa;

import encrypt.EncryptUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static java.lang.System.out;
import static org.bouncycastle.asn1.x509.ObjectDigestInfo.publicKey;

public class TestRSA {
    private static final String SRC = "I'm RSA encryption algorithm";

    public static void main(String[] args) {
        jdkRsa();
    }


    /**
     * JDK-RSA算法实现
     */
    private static void jdkRsa() {
        try {
            // 初始化密钥
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

            out.println("public key is : " + EncryptUtil.bytesToHex(rsaPublicKey.getEncoded()));
            out.println("private key is : " + EncryptUtil.bytesToHex(rsaPrivateKey.getEncoded()));

            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
            keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

            long start = System.currentTimeMillis();
            out.println("开始：" + start);
            for (int i = 0; i < 100_000; i++) {
                // 公钥加密
                byte[] res = encrypt(publicKey);
                // 私钥解密
                decrypt(privateKey, res);
            }
            long end = System.currentTimeMillis();
            // 耗时 转为秒
            out.println("耗时：" + (end - start) / 1000.0D + "秒");

            // 签名
            byte[] signature = sign(privateKey, SRC.getBytes());
            // 验签
            if (verify(publicKey, SRC.getBytes(), signature)) {
                out.println("验证签名成功");
            } else {
                out.println("验证签名失败");
            }

        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void decrypt(PrivateKey privateKey, byte[] res) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            res = cipher.doFinal(res);
            out.println("私钥解密: " + new String(res));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] encrypt(PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] res = cipher.doFinal(SRC.getBytes(StandardCharsets.UTF_8));
            out.println("公钥加密: " + EncryptUtil.bytesToHex(res));
            return res;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 签名
     */
    public static byte[] sign(PrivateKey privateKey, byte[] data) throws Exception {
        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(privateKey);
        signer.update(data);
        return signer.sign();
    }

    /**
     * 验签
     */
    public static boolean verify(PublicKey publicKey, byte[] data, byte[] signature) throws Exception {
        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(publicKey);
        verifier.update(data);
        return verifier.verify(signature);
    }

}
