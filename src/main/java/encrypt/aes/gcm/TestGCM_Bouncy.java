package encrypt.aes.gcm;

import encrypt.EncryptUtil;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TestGCM_Bouncy {
    private static final int AES_KEY_SIZE = 128; // 128位密钥
    private static final int GCM_IV_LENGTH = 12; // GCM推荐IV长度12字节
    private static final int GCM_TAG_LENGTH = 16; // GCM认证标签长度16字节（128位）

    public static void main(String[] args) {
        System.out.println("Default Charset: 检查" + Charset.defaultCharset());
        // 示例用法
        byte[] key = "1234567890123456".getBytes();
        String originalText = "Hello, AES-GCM!";

        byte[] iv = "123456789012".getBytes();

        // 加密
        byte[] encrypted = encrypt(originalText.getBytes(), key, iv);
        System.out.println("加密结果 (Hex): " + EncryptUtil.bytesToHexString(encrypted));

        // 解密
        String decryptedText = decrypt(encrypted, key, iv);
        System.out.println("解密结果: " + decryptedText);
    }

    /**
     * AES-GCM加密字符串
     * @param plaintext 明文
     * @param key 密钥
     * @return 密文结构：IV (12字节) + 加密数据 + 认证标签 (16字节)
     */
    public static byte[] encrypt(byte[] plaintext, byte[] key,  byte[] iv) {
        try {
            // 2. 初始化加密器
            GCMBlockCipher cipher = new GCMBlockCipher(new AESEngine());
            AEADParameters params = new AEADParameters(
                    new KeyParameter(key),
                    GCM_TAG_LENGTH * 8, // 标签长度（位）
                    iv,
                    null // 无关联数据
            );
            cipher.init(true, params);

            // 3. 处理数据
            byte[] input = plaintext;
            byte[] output = new byte[cipher.getOutputSize(input.length)];

            int len = cipher.processBytes(input, 0, input.length, output, 0);
            cipher.doFinal(output, len); // 生成认证标签

            return output;
            // 4. 组合IV + 密文 + 标签
//            byte[] result = new byte[iv.length + output.length];
//            System.arraycopy(iv, 0, result, 0, iv.length);
//            System.arraycopy(output, 0, result, iv.length, output.length);
//            return result;
        } catch (Exception e) {
            throw new RuntimeException("加密失败", e);
        }
    }

    /**
     * AES-GCM解密字符串
     * @param ciphertext 密文（IV + 加密数据 + 标签）
     * @param key 密钥
     * @return 解密后的明文
     */
    public static String decrypt(byte[] ciphertext, byte[] key, byte[] iv) {
        try {
            // 1. 提取IV、密文和标签
//            byte[] iv = new byte[GCM_IV_LENGTH];
//            System.arraycopy(ciphertext, 0, iv, 0, iv.length);

//            byte[] encryptedDataWithTag = new byte[ciphertext.length - iv.length];
            byte[] encryptedDataWithTag = ciphertext;
//            System.arraycopy(ciphertext, iv.length, encryptedDataWithTag, 0, encryptedDataWithTag.length);

            // 2. 初始化解密器
            GCMBlockCipher cipher = new GCMBlockCipher(new AESEngine());
            AEADParameters params = new AEADParameters(
                    new KeyParameter(key),
                    GCM_TAG_LENGTH * 8,
                    iv,
                    null
            );
            cipher.init(false, params);

            // 3. 解密并验证标签
            byte[] output = new byte[cipher.getOutputSize(encryptedDataWithTag.length)];
            int len = cipher.processBytes(encryptedDataWithTag, 0, encryptedDataWithTag.length, output, 0);
            cipher.doFinal(output, len); // 验证标签，失败则抛异常

            // 4. 转换为字符串
            return new String(output, StandardCharsets.UTF_8).trim();
        } catch (Exception e) {
            throw new RuntimeException("解密失败（可能数据被篡改或密钥错误）", e);
        }
    }

}
