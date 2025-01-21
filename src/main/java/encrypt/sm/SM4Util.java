package encrypt.sm;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;

public class SM4Util {
//    private final String ALGORITHM = "SM4/ECB/PKCS5Padding"; // 电子密码本模式
//    private final String ALGORITHM = "SM4/CBC/PKCS5Padding"; // 分组连接模式
//    private final String ALGORITHM = "SM4/CFB8/NoPadding"; // 密文反馈模式
//    private final String ALGORITHM = "SM4/OFB8/NoPadding"; // 输出反馈模式
//    private final String ALGORITHM = "SM4/CTR/NoPadding"; // 计数器模式
    private final String ALGORITHM = "SM4/GCM/NoPadding";
    private byte[] iv = new byte[16];

    public String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey("SM4"), getIVParameter());
            byte[] result = cipher.doFinal(plainText.getBytes());
            return HexUtil.bytesToHex(result);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    public String decrypt(String cipherText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey("SM4"), new IvParameterSpec(iv));
            byte[] result = cipher.doFinal(HexUtil.hexStringToByteArray(cipherText));
            return new String(result, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    private Key getSecretKey(String name) {
        // 密钥只能是128位(16字节)
        byte[] keyBytes = "1234567890abcdef".getBytes();
        // 初始化 SecretKeySpec
        return new SecretKeySpec(keyBytes, name);
    }

    private IvParameterSpec getIVParameter() {
        // SecureRandom 生成随机iv数组 长度16
        byte[] iv = new byte[16];
        try {
            SecureRandom.getInstanceStrong().nextBytes(iv);
            this.iv = iv;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return new IvParameterSpec(iv);
    }
}
