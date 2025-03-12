package encrypt.mac;

import encrypt.EncryptUtil;
import encrypt.aes.gcm.TestGCM_Bouncy;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.macs.GMac;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.modes.GCMModeCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class TestGMAC {
    public static void main(String[] args) throws Exception {
        // 生成 AES 密钥
        byte[] keyBytes = "1234567890123456".getBytes(); // 128位密钥
//        byte[] keyBytes = "12345678901234561234567890123456".getBytes(); // 256位密钥

        // 待认证的数据
        String data = "Hello GMAC";
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);

        // 生成随机 IV（12字节）
        byte[] iv = "123456789012".getBytes();

        // 计算 GMAC
        // 54b31e53651d34f170f8965e6d1f5ba2
        // 3023701e51f8d8551ed1fa0f7dee747a
        String gmac = calculateGMAC(dataBytes, keyBytes, iv);
        System.out.println("GMAC1: " + gmac);

        // 加密并获取 GMAC（认证标签）
        byte[] ciphertext = encryptWithGMAC(dataBytes, keyBytes, iv);
        System.out.println("ciphertextStr:" + EncryptUtil.bytesToHex(ciphertext));
        String gmac2 = getGMACTag(ciphertext);
        System.out.println("GMAC2: " + gmac2);
        byte[] decrypted = decryptWithGMAC(ciphertext, keyBytes, iv);
        System.out.println("decryptStr:" + new String(decrypted));
    }

    // 计算 GMAC
    public static String calculateGMAC(byte[] data, byte[] keyBytes, byte[] iv) {
        GCMModeCipher gcmCipher = GCMBlockCipher.newInstance(AESEngine.newInstance());
        GMac gMac = new GMac(gcmCipher, 128); // 128位标签
        KeyParameter keyParam = new KeyParameter(keyBytes);
        ParametersWithIV params = new ParametersWithIV(keyParam, iv);
        gMac.init(params);
        gMac.update(data, 0, data.length);
        byte[] mac = new byte[gMac.getMacSize()];
        gMac.doFinal(mac, 0);
        return EncryptUtil.bytesToHex(mac);
    }

    // 使用 AES-GCM 加密
    public static byte[] encryptWithGMAC(byte[] data, byte[] key, byte[] iv) throws Exception {
        SecretKey secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv); // 128位认证标签
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmSpec);
        return cipher.doFinal(data);
    }

    // 使用 AES-GCM 解密
    public static byte[] decryptWithGMAC(byte[] data, byte[] key, byte[] iv) throws Exception {
        SecretKey secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv); // 128位认证标签
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmSpec);
        return cipher.doFinal(data);
    }

    // 从加密结果中提取 GMAC 标签（最后 16 字节）
    public static String getGMACTag(byte[] encryptedData) {
        int tagLength = 16; // 128位 = 16字节
        byte[] tag = new byte[tagLength];
        System.arraycopy(encryptedData, encryptedData.length - tagLength, tag, 0, tagLength);
        return EncryptUtil.bytesToHex(tag);
    }
}
