package encrypt.aes.text;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class TestAES_TextEncrypt {
    public static void main(String[] args) throws Exception {
        // 需要加密的字串
        String cSrc = "www.baidu.com";
        System.out.println(cSrc);
        // 加密
        String enString = encryptAES(cSrc);
        System.out.println("加密后的字串是：" + enString);

        // 解密
        String DeString = decryptAES(enString);
//        String DeString = AESTextEncryptTest.decryptAES("kuXNoFjsbAo3mN79uD6oqA==");
        System.out.println("解密后的字串是：" + DeString);
    }

    /**
     * 加密用的Key 可以用26个字母和数字组成 使用AES-128-CBC加密模式，key需要为16位。
     */
//    private static final String key = "hj7x89H$yuBI0456";
    private static final String key = "uBdUx82vPHkDKb284d7NkjFoNcKWBuka";
    private static final String iv = "NIfb&95GUY86Gfgh";

    /**
     * @param data 明文
     * @return 密文
     * @author miracle.qu
     * @Description AES算法加密明文
     */
    public static String encryptAES(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] dataBytes = data.getBytes();

            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());  // CBC模式，需要一个向量iv，可增加加密算法的强度

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(dataBytes);

            return encode(encrypted).trim(); // BASE64做转码。

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param data 密文
     * @return 明文
     * @author miracle.qu
     * @Description AES算法解密密文
     */
    public static String decryptAES(String data) {
        try {
            byte[] encrypted1 = decode(data);//先用base64解密

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString.trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 编码
     *
     * @param byteArray
     * @return
     */
    public static String encode(byte[] byteArray) {
        return new String(Base64.getEncoder().encodeToString(byteArray));
    }

    /**
     * 解码
     *
     * @param base64EncodedString
     * @return
     */
    public static byte[] decode(String base64EncodedString) {
        return Base64.getDecoder().decode(base64EncodedString);
    }
}
