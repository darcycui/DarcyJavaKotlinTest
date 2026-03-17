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
 * 模拟 96位IV 32位计数器
 * |<------- 128 位块 ------->|
 * |---- 96 位 ----|-- 32 位 --|
 * |     Nonce     | Counter  |
 * | (你的 iv)     | 从 1 开始 |
 */
public class TestAES_CTR_Counter {
    public static void main(String[] args) throws InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
        // 使用全0的密钥和IV，便于观察
        byte[] key = "1234567812345678".getBytes(StandardCharsets.UTF_8);  // 密钥
        byte[] iv = AESCTRIVHelper.getCounterNonceIv((byte) 1);   // 模拟 96位IV 32位计数器

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
        // 密文:da49075cf5001ba77ddd5043195437b5

        // 如果Java使用96+32模式，块1和块2的差异应该是计数器部分
        // 但实际上，差异显示整个128位都在变化

        // 解密
        Cipher cipherDecrypt = Cipher.getInstance("AES/CTR/NoPadding");
        cipherDecrypt.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] plaintext = cipherDecrypt.doFinal(ciphertext);
        System.out.println("明文:" + EncryptUtil.toNormalString(plaintext));

    }

}
