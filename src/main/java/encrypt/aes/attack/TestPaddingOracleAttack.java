package encrypt.aes.attack;

import encrypt.EncryptUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

/**
 * 测试 Padding Oracle Attack 填充错误检测攻击
 */
public class TestPaddingOracleAttack {
    private static final String iv = "AbcdefAbcdefAbcd";
    private static final String key = "1234567890abcdef1234567890abcdef";
    private static final String plainText = "BRIAN;12";

    public static void main(String[] args) throws Exception {
        System.out.println("明文：" + plainText);
        String encryptText = encrypt(plainText);
        System.out.println("密文：" + encryptText); // 832848775be3d55cdb472885634450fd
        String decryptText = decrypt(encryptText, iv.getBytes());
        System.out.println("解密：" + decryptText);

        // 创建一个newIV 128位数组
//        byte[] newIV = new byte[16];
//        for (int j = 14; j >= 0; j--) {
//            for (int i = 0; i < 255; i++) {
//                newIV[j] = (byte) i;
//                if (PaddingOracle.isPaddingValid(EncryptUtil.hexStringToByteArray(encryptText), newIV)) {
//                    System.out.println("Found new IV: " + EncryptUtil.bytesToHex(newIV));
//                    break;
//                }
//            }
//        }
//        System.out.println("newIV: " + EncryptUtil.bytesToHex(newIV));
    }

    public static String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes();

            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());  // CBC模式，需要一个向量iv，可增加加密算法的强度

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(dataBytes);

            return EncryptUtil.bytesToHex(encrypted); // 数组转16进制string

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String data, byte[] newIV) throws Exception {
        byte[] encrypted1 = EncryptUtil.hexStringToByteArray(data); //16进制string转数组
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(newIV);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original);
        return originalString.trim();
    }
}
