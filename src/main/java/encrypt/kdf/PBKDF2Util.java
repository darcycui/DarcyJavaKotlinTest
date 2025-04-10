package encrypt.kdf;

import encrypt.EncryptUtil;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class PBKDF2Util {

    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA512";

    private static final int HASH_BIT_SIZE = 32 * 8;         //生成密文的长度
    private static final int PBKDF2_ITERATIONS = 25600;

    /**
     * 生成密文
     *
     * @param password 明文密码
     * @param salt     盐值
     */
    public static String getHashedPassword(String password, String salt) {

        KeySpec spec = new PBEKeySpec(password.toCharArray(), EncryptUtil.hexStringToByteArray(salt), PBKDF2_ITERATIONS, HASH_BIT_SIZE);
        SecretKeyFactory keyFactory = null;
        try {
            keyFactory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
            return EncryptUtil.bytesToHexString(keyFactory.generateSecret(spec).getEncoded());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return password;
    }

    public static boolean checkPassword(String password, String hashedPassword, String salt) {
        String encryptedPassword = getHashedPassword(password, salt);
        if (encryptedPassword.equals(hashedPassword)) {
            return true;
        }
        return false;
    }
}
