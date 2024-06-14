package encrypt.aes;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.security.*;
import java.util.Arrays;

public class AESPaddingTest {

    public static void main(String[] args) {
        String inputFile = "D:\\aaa\\ScreenShot\\Screenshot_20240131-083143.png";
        String encryptedFile = "D:\\aaa\\ScreenShot\\Screenshot_20240131-083143-encryptedFile.jpg";
        String decryptedFile = "D:\\aaa\\ScreenShot\\Screenshot_20240131-083143-decryptedFile.jpg";
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

    public static void encryptFile(String inputFile, String outputFile, String keyString) {
        FileInputStream ins = null;
        FileOutputStream outs = null;
        try {
            // 生成随机的初始化向量
            SecureRandom random = new SecureRandom();
            byte[] ivBytes = new byte[16];
            random.nextBytes(ivBytes);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            // 创建密钥对象
            byte[] keyBytes = Arrays.copyOf(keyString.getBytes("UTF-8"), 16);
            SecretKey key = new SecretKeySpec(keyBytes, "AES");

            // 初始化加密器
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            // 打开输入和输出文件流
            ins = new FileInputStream(inputFile);
            outs = new FileOutputStream(outputFile);

            // 写入初始化向量到输出文件
            outs.write(ivBytes);

            // 加密文件内容
            byte[] inputBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = ins.read(inputBuffer)) != -1) {
                byte[] outputBuffer = cipher.update(inputBuffer, 0, bytesRead);
                outs.write(outputBuffer);
            }
            byte[] outputBuffer = cipher.doFinal();
            outs.write(outputBuffer);


        } catch (IOException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException |
                 InvalidAlgorithmParameterException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        } finally {
            // 关闭文件流
            try {
                if (ins != null) {
                    ins.close();
                }
                if (outs != null) {
                    outs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void decryptFile(String inputFile, String outputFile, String keyString) {
        FileInputStream ins = null;
        FileOutputStream outs = null;

        try {
            // 读取初始化向量
            ins = new FileInputStream(inputFile);
            byte[] ivBytes = new byte[16];
            ins.read(ivBytes);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            // 创建密钥对象
            byte[] keyBytes = Arrays.copyOf(keyString.getBytes("UTF-8"), 16);
            SecretKey key = new SecretKeySpec(keyBytes, "AES");

            // 初始化解密器
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, iv);

            // 打开输入和输出文件流
            outs = new FileOutputStream(outputFile);

            // 解密文件内容
            byte[] inputBuffer = new byte[16];
            int bytesRead;
            while ((bytesRead = ins.read(inputBuffer)) != -1) {
                byte[] outputBuffer = cipher.update(inputBuffer, 0, bytesRead);
                outs.write(outputBuffer);
            }
            byte[] outputBuffer = cipher.doFinal();
            outs.write(outputBuffer);

        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        } finally {
            // 关闭文件流
            try {
                if (ins != null) {
                    ins.close();
                }
                if (outs != null) {
                    outs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

