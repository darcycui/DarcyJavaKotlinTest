package encrypt.aes.padding;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.util.Arrays;

public class TestAES_NoPadding {

    public static void main(String[] args) {
        String inputFile = "D:\\aaa\\ScreenShot\\Screenshot_20240131-083143 - 副本.png";
        String encryptedFile = "D:\\aaa\\ScreenShot\\Screenshot_20240131-083143 - 副本-encryptedFile.jpg";
        String decryptedFile = "D:\\aaa\\ScreenShot\\Screenshot_20240131-083143 - 副本-decryptedFile.jpg";
        String keyString = "yourKey123456789"; // 16字节的密钥

        try {
            // 加密
            encryptFile(inputFile, encryptedFile, keyString);

            // 解密
            decryptFile(encryptedFile, decryptedFile, keyString);

            System.out.println("加密解密成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void encryptFile(String inputFile, String outputFile, String keyString) throws Exception {
        FilePaddingHelper.addPadding0(inputFile);
        // 生成随机的初始化向量
        SecureRandom random = new SecureRandom();
        byte[] ivBytes = new byte[16];
        random.nextBytes(ivBytes);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        // 创建密钥对象
        byte[] keyBytes = Arrays.copyOf(keyString.getBytes("UTF-8"), 16);
        SecretKey key = new SecretKeySpec(keyBytes, "AES");

        // 初始化加密器
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        // 打开输入和输出文件流
        FileInputStream in = new FileInputStream(inputFile);
        FileOutputStream out = new FileOutputStream(outputFile);

        // 写入初始化向量到输出文件
        out.write(ivBytes);

        // 加密文件内容
        byte[] inputBuffer = new byte[16];
        int bytesRead;
        while ((bytesRead = in.read(inputBuffer)) != -1) {
            byte[] outputBuffer = cipher.update(inputBuffer, 0, bytesRead);
            out.write(outputBuffer);
        }
        byte[] outputBuffer = cipher.doFinal();
        out.write(outputBuffer);

        // 关闭文件流
        in.close();
        out.close();
    }

    public static void decryptFile(String inputFile, String outputFile, String keyString) throws Exception {
        // 读取初始化向量
        FileInputStream in = new FileInputStream(inputFile);
        byte[] ivBytes = new byte[16];
        in.read(ivBytes);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        // 创建密钥对象
        byte[] keyBytes = Arrays.copyOf(keyString.getBytes("UTF-8"), 16);
        SecretKey key = new SecretKeySpec(keyBytes, "AES");

        // 初始化解密器
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        // 打开输入和输出文件流
        FileOutputStream out = new FileOutputStream(outputFile);

        // 解密文件内容
        byte[] inputBuffer = new byte[16];
        int bytesRead;
        while ((bytesRead = in.read(inputBuffer)) != -1) {
            byte[] outputBuffer = cipher.update(inputBuffer, 0, bytesRead);
            out.write(outputBuffer);
        }
        byte[] outputBuffer = cipher.doFinal();
        out.write(outputBuffer);

        // 关闭文件流
        in.close();
        out.close();
    }
}

