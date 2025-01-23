package encrypt.dh;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

public class AESKeyUtil {
    /**
     * 使用共享密钥生成 AES 密钥
     */
    private static SecretKey generateAESKeyFromSharedSecret(byte[] sharedSecret) {
        // 创建一个 AES 密钥生成器
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("AES");
            // 获取 AES 密钥的长度（例如 128、192 或 256 位）
            int keySize = 256; // 根据需要调整
            keyGen.init(keySize);
            // 使用共享的秘密值生成 AES 密钥
            // 注意：这里我们简单地截取共享秘密值的前 keySize/8 字节
            byte[] keyBytes = new byte[keySize / 8];
            System.arraycopy(sharedSecret, 0, keyBytes, 0, keyBytes.length);
            return new SecretKeySpec(keyBytes, "AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
