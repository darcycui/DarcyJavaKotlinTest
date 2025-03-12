package encrypt.mac;

import encrypt.EncryptUtil;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TestHMAC {
    public static void main(String[] args) throws Exception {
        String keyStr = "1234567890abcdef";
        String message = "Hello Tom";
        System.out.println("message=" + message);
        // 计算 message 的 HAMC
        String hmac = computeHmacSha256(keyStr, message);
        System.out.println("hmac=" + hmac);
        // 计算 message+key 的 Hash
        String hash = computeSha256(message + keyStr);
        System.out.println("hash=" + hash);
    }

    public static String computeHmacSha256(String key, String message) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKey);
        byte[] result = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return EncryptUtil.bytesToHex(result);
    }

    public static String computeSha256(String message) {
        // 获取MessageDigest实例，指定算法为SHA-256
        MessageDigest digest = null;
        String result = "";
        try {
            digest = MessageDigest.getInstance("SHA-256");
            // 计算输入字符串的哈希值
            byte[] hashBytes = digest.digest(message.getBytes());
            result = EncryptUtil.bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
