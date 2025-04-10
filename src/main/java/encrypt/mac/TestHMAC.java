package encrypt.mac;

import encrypt.EncryptUtil;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class TestHMAC {
    public static void main(String[] args) throws Exception {
        // 密钥（16字节）
//        String keyStr = "1234567890123456";
        // 密钥（32字节）
        String keyStr = "12345678901234561234567890123456";
        String message = "Hello, HMAC!";
        System.out.println("message=" + message);
        // 计算 message 的 HAMC 长度取决于使用的hash算法
        // 117881d3d4fe355d726ee8fc0852ec3267f49f83772632e6de19d6afc0d9dedb
        // 9050466246db7c3d7bd3f27801d633aad72b3fd0b764770918fe4b4ee16149ce
        String hmac = computeHmacSha256(keyStr, message);
        System.out.println("hmac=" + hmac);
        // 计算 message+key 的 Hash 长度取决于使用的hash算法
        // 0363e201ea205f2714875a4f6274f72e8212c8ca3800377f0e0fb094bfd022f9
        // d9e1f4b526d9f16d3cf51c1969eca21976abfde8b2dc8a92d055c15eada104b6
        String hash = computeSha256(message + keyStr);
        System.out.println("hash=" + hash);
    }

    public static String computeHmacSha256(String key, String message) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKey);
        byte[] result = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return EncryptUtil.bytesToHexString(result);
    }

    public static String computeSha256(String message) {
        // 获取MessageDigest实例，指定算法为SHA-256
        MessageDigest digest = null;
        String result = "";
        try {
            digest = MessageDigest.getInstance("SHA-256");
            // 计算输入字符串的哈希值
            byte[] hashBytes = digest.digest(message.getBytes());
            result = EncryptUtil.bytesToHexString(hashBytes);
        } catch (Exception e) {
            System.err.println("计算HMAC失败:" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
