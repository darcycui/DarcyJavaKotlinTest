package encrypt.aes.ctr;

import encrypt.EncryptUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * AES CTR模式 加密
 * 默认全部128位作为计数器
 * |<--------- 128 位 --------->|
 * |--- 整个 128 位作为计数器 ---|
 * |  1234567812345678 (初始值) |
 */
public class TestAES_CTR_IV {
    public static void main(String[] args) throws InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
        // 使用全0的密钥和IV，便于观察
        byte[] key = "1234567812345678".getBytes(StandardCharsets.UTF_8);  // 密钥
        byte[] iv = "1234567812345678".getBytes(StandardCharsets.UTF_8);   // 128位IV 默认全部128位作为计数器

        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        // 加密多个块，观察密钥流
        HexFormat hex = HexFormat.of();
        byte[] originalText = "abcdefghijklmnhi".getBytes(StandardCharsets.UTF_8);
        System.out.println("原文:" + EncryptUtil.toNormalString(originalText));

        byte[] ciphertext = cipher.doFinal(originalText);
        System.out.println("密文:" + hex.formatHex(ciphertext));
        // 密文:0cce7f3282219d8853a5e704fc8a4089

        // 解密
        Cipher cipherDecrypt = Cipher.getInstance("AES/CTR/NoPadding");
        cipherDecrypt.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] plaintext = cipherDecrypt.doFinal(ciphertext);
        System.out.println("明文:" + EncryptUtil.toNormalString(plaintext));

    }

}
