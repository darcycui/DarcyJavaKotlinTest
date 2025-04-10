package encrypt.kdf;

import encrypt.EncryptUtil;
import org.bouncycastle.crypto.generators.SCrypt;

import java.nio.charset.StandardCharsets;

public class ScryptUtil {
    // Scrypt参数
    private static final int N = 16384; // CPU/内存开销参数，必须是2的幂
    private static final int R = 8; // 块大小参数
    private static final int P = 1; // 并行化参数
    private static final int KEY_LENGTH = 32; // 输出密钥长度（字节）

    public static String getHashedPassword(String password, String salt) {
        // 计算Scrypt哈希
        byte[] result = SCrypt.generate(
                password.getBytes(StandardCharsets.UTF_8),
                salt.getBytes(StandardCharsets.UTF_8),
                N,
                R,
                P,
                KEY_LENGTH
        );
        return EncryptUtil.bytesToHexString(result);
    }
    public static boolean checkPassword(String password, String hashedPassword, String salt) {
        String encryptedPassword = getHashedPassword(password, salt);
        if (encryptedPassword.equals(hashedPassword)) {
            return true;
        }
        return false;
    }
}
