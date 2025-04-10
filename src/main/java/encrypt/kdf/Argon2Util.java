package encrypt.kdf;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class Argon2Util {

    // 安全参数（需根据场景调整）
    private static final int ITERATIONS = 2;      // 迭代次数（示例值，实际需根据测试配置）
    private static final int MEMORY = 65536;      // 内存大小（KB，此处 64MB）
    private static final int PARALLELISM = 2;     // 并行线程数
    private static final int SALT_LENGTH = 16;     // 盐长度（16字节）
    private static final int HASH_LENGTH = 32;     // 生成哈希长度（32字节）

    public static String getHashedPassword(String password) {
        // 初始化 Argon2 实例（推荐使用 Argon2id）
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

        try {
            // 生成哈希（自动生成盐并嵌入哈希结果）
            String result = argon2.hash(ITERATIONS, MEMORY, PARALLELISM, password.toCharArray());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            argon2.wipeArray(password.toCharArray()); // 清空密码内存
        }
        return password;
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        return argon2.verify(hashedPassword, password.toCharArray());
    }
}
