package encrypt.aes.attack;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

public class PaddingOracle {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final byte[] KEY = "1234567890abcdef1234567890abcdef".getBytes(); // 16 bytes key for AES-128

    public static boolean isPaddingValid(byte[] ciphertext, byte[] newIV) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec secretKeySpec = new SecretKeySpec(KEY, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(newIV);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            cipher.doFinal(ciphertext);
            return true; // Padding is valid
        } catch (Exception e) {
            return false; // Padding is invalid
        }
    }
}
