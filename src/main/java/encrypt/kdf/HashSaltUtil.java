package encrypt.kdf;

import encrypt.EncryptUtil;

import java.security.MessageDigest;

public class HashSaltUtil {

    public static String getHashedPassword(String password, String salt) {
        // 将 password 与 salt 拼接
        String message = password + salt;
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

    public static boolean checkPassword(String password, String hashedPassword, String salt) {
        String encryptedPassword = getHashedPassword(password, salt);
        if (encryptedPassword.equals(hashedPassword)) {
            return true;
        }
        return false;
    }
}
