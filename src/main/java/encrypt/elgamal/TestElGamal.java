package encrypt.elgamal;

import encrypt.EncryptUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.DHParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static java.lang.System.out;

/**
 * ElGamal加密算法实现
 */
public class TestElGamal {

    private static final String SRC = "I'm ElGamal encryption algorithm";

    public static void main(String[] args) {
        bcElGamal();
    }

    private static void bcElGamal() {
        try {
            Security.addProvider(new BouncyCastleProvider());

            AlgorithmParameterGenerator algorithmParameterGenerator = AlgorithmParameterGenerator.getInstance("ElGamal");
            algorithmParameterGenerator.init(256);
            AlgorithmParameters algorithmParameters = algorithmParameterGenerator.generateParameters();
            DHParameterSpec dhParameterSpec = algorithmParameters.getParameterSpec(DHParameterSpec.class);

            // 初始化密钥
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ElGamal");
            keyPairGenerator.initialize(dhParameterSpec, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey elGamalPublicKey = keyPair.getPublic();
            PrivateKey elGamalPrivateKey = keyPair.getPrivate();

            out.println("public key is : " + EncryptUtil.bytesToHex(elGamalPublicKey.getEncoded()));
            out.println("private key is : " + EncryptUtil.bytesToHex(elGamalPrivateKey.getEncoded()));

            // 公钥加密，私钥解密 -- 加密
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(elGamalPublicKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("ElGamal");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance("ElGamal");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] result = cipher.doFinal(SRC.getBytes(StandardCharsets.UTF_8));
            out.println("私钥加密，公钥解密 -- 加密 : " + EncryptUtil.bytesToHex(result));

            // 公钥加密，私钥解密 -- 解密
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(elGamalPrivateKey.getEncoded());
            keyFactory = KeyFactory.getInstance("ElGamal");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            result = cipher.doFinal(result);
            out.println("私钥加密，公钥解密 -- 解密 : " + new String(result));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException | InvalidParameterSpecException |
                 InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

}

