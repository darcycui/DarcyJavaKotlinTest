package encrypt.kdf;

import org.mindrot.jbcrypt.BCrypt;

public class BcryptUtil {
    private static final int LOG_ROUNDS = 12; // 指定计算成本（默认10，范围4~31，每+1计算时间翻倍） 建议值：10~12（兼顾安全性和性能）

    public static String getHashedPassword(String password) {
        String salt = BCrypt.gensalt(LOG_ROUNDS);
        return BCrypt.hashpw(password, salt);
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
