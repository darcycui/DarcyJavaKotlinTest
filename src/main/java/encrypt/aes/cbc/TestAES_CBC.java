package encrypt.aes.cbc;

import encrypt.EncryptUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class TestAES_CBC {
    public static void main(String[] args) throws Exception {
        // 需要加密的字串
        String cSrc = "www.baidu.com";
        System.out.println("明文是：" + cSrc + "长度：" + cSrc.length());
        String hashPlainText = EncryptUtil.hash256(cSrc.getBytes());
        System.out.println("明文的hash256是：" + hashPlainText);
        // 加密
        String enString = encryptAES(cSrc);
        System.out.println("加密后的字串是：" + enString);

        // 解密
        String deString = decryptAES(enString);
        System.out.println("解密后的字串是：" + deString + "长度：" + deString.length());
        String hashDeString = EncryptUtil.hash256(deString.getBytes());
        System.out.println("解密后的hash256是：" + hashDeString);
    }

    /**
     * 加密用的Key 可以用32字节 或16字节。
     * 加密用的IV 只能是16字节
     */
    private static final String key = "1234567890abcdef1234567890abcdef";
    private static final String iv = "1234567890abcdef";

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

            return encode(encrypted); // base64 编码。

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param data 密文
     * @return 明文
     * @author miracle.qu
     * @Description AES 算法解密密文
     */
    public static String decryptAES(String data) {
        try {
            byte[] encrypted1 = decode(data); // 先用base64解码

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            // todo注意：这里使用 NoPadding也是可以解密的 末尾会多出1-16字节的填充
//            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] original = cipher.doFinal(encrypted1);
            return new String(original);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
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
