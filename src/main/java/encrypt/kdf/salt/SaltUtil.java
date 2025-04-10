package encrypt.kdf.salt;

import encrypt.EncryptUtil;

import java.security.SecureRandom;

public class SaltUtil {
    // 生成随机盐字符串(16进制)
    public static String generateSaltString(int length) {
        byte[] array = generateSaltArray(length);
        return EncryptUtil.bytesToHexString(array);
    }

    // 生成随机盐数组
    public static byte[] generateSaltArray(int length) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return salt;
    }
}
