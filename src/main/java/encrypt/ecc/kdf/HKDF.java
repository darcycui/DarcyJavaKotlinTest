package encrypt.ecc.kdf;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HKDF {
    private static final int HASH_OUTPUT_SIZE = 32;

    public byte[] deriveSecrets(byte[] inputKeyMaterial, byte[] salt, byte[] info, int outputLength) {
        byte[] prk = extract(salt, inputKeyMaterial);
        return expand(prk, info, outputLength);
    }

    /**
     * 导出密钥
     * @param salt 盐
     * @param inputKeyMaterial 输入密钥
     * @return 密钥
     */
    private byte[] extract(byte[] salt, byte[] inputKeyMaterial) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(salt, "HmacSHA256"));
            return mac.doFinal(inputKeyMaterial);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * 拓展密钥到指定长度
     * @param prk 密钥
     * @param info 附加信息
     * @param outputSize 指定长度(字节)
     * @return 密钥
     */
    private byte[] expand(byte[] prk, byte[] info, int outputSize) {
        try {
            int iterations = (int) Math.ceil((double) outputSize / (double) HASH_OUTPUT_SIZE);
            byte[] mixin = new byte[0];
            ByteArrayOutputStream results = new ByteArrayOutputStream();
            int remainingBytes = outputSize;
            for (int i = getIterationStartOffset(); i < iterations + getIterationStartOffset(); i++) {
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(prk, "HmacSHA256"));
                mac.update(mixin);
                if (info != null) {
                    mac.update(info);
                }
                mac.update((byte) i);
                byte[] stepResult = mac.doFinal();
                int stepSize = Math.min(remainingBytes, stepResult.length);
                results.write(stepResult, 0, stepSize);
                mixin = stepResult;
                remainingBytes -= stepSize;
            }
            return results.toByteArray();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new AssertionError(e);
        }
    }

    private int getIterationStartOffset() {
        return 1;
    }
}
