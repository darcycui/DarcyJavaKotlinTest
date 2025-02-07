package encrypt.aes;

import encrypt.EncryptUtil;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;

import java.security.SecureRandom;

public class TestAES_CCM {
    public static void main(String[] args) throws Exception {
        String originalString = "Hello, World!";
        String keyString = "0123456789abcdef0123456789abcdef"; // 32 bytes key for AES-256
        String aad = "Additional Authenticated Data";
        System.out.println("Original string: " + originalString);

        // 密钥（16字节）
        byte[] key = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(key);

        // 随机生成的初始化向量（IV），长度为12字节
        byte[] nonce = new byte[12];
        random.nextBytes(nonce);

        // 要加密的数据
        byte[] plaintext = originalString.getBytes();

        // 加密
        byte[] ciphertext = encrypt(keyString.getBytes(), nonce, plaintext, aad.getBytes());
        System.out.println("Encrypt: " + EncryptUtil.bytesToHex(ciphertext));

        // 解密
        byte[] decryptedText = decrypt(keyString.getBytes(), nonce, ciphertext, aad.getBytes());

        System.out.println("Decrypted: " + new String(decryptedText));
    }

    public static byte[] encrypt(byte[] key, byte[] nonce, byte[] plaintext, byte[] associatedData) throws Exception {
        AESEngine engine = new AESEngine();
        CCMBlockCipher ccmBlockCipher = new CCMBlockCipher(engine);
        KeyParameter keyParam = new KeyParameter(key);
        AEADParameters params = new AEADParameters(keyParam, 128, nonce, associatedData);
        ccmBlockCipher.init(true, params);

        byte[] ciphertext = new byte[ccmBlockCipher.getOutputSize(plaintext.length)];
        int length = ccmBlockCipher.processBytes(plaintext, 0, plaintext.length, ciphertext, 0);
        ccmBlockCipher.doFinal(ciphertext, length);

        return ciphertext;
    }

    public static byte[] decrypt(byte[] key, byte[] nonce, byte[] ciphertext, byte[] associatedData) throws Exception {
        AESEngine engine = new AESEngine();
        CCMBlockCipher ccmBlockCipher = new CCMBlockCipher(engine);
        KeyParameter keyParam = new KeyParameter(key);
        AEADParameters params = new AEADParameters(keyParam, 128, nonce, associatedData);
        ccmBlockCipher.init(false, params);

        byte[] decryptedText = new byte[ccmBlockCipher.getOutputSize(ciphertext.length)];
        int length = ccmBlockCipher.processBytes(ciphertext, 0, ciphertext.length, decryptedText, 0);
        ccmBlockCipher.doFinal(decryptedText, length);

        return decryptedText;
    }
}

