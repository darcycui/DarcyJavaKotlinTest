package encrypt.aes.gcm;

import encrypt.EncryptUtil;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;

import static java.lang.System.out;

public class TestAES_GCM {
    private static final int GCM_TAG_LENGTH = 16; // tag in bytes
    private static final int GCM_IV_LENGTH = 12; // iv in bytes

    public static void main(String[] args) throws Exception {
        String originalString = "Hello, World!";
        String keyString = "0123456789abcdef0123456789abcdef"; // 32 bytes key for AES-256
        String aad = "Additional Authenticated Data";
        System.out.println("Original string: " + originalString);

        // Convert key string to SecretKey
        SecretKey key = new SecretKeySpec(keyString.getBytes(), "AES");
        long start = System.currentTimeMillis();
        out.println("开始：" + start);
        for (int i = 0; i < 100_000; i++) {
            // Encrypt the original string
            byte[] encryptedData = encrypt(originalString, key, aad);
            System.out.println("Encrypted data: " + EncryptUtil.bytesToHex(encryptedData));

            // Decrypt the encrypted data
            String decryptedString = decrypt(encryptedData, key, aad);
            System.out.println("Decrypted string: " + decryptedString);
        }
        long end = System.currentTimeMillis();
        // 耗时 转为秒
        out.println("耗时：" + (end - start) / 1000.0D + "秒");
    }

    public static byte[] encrypt(String plainText, SecretKey key, String aad) throws Exception {
        // Generate a random IV
        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        // Create GCMParameterSpec
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);

        // Create a Cipher instance and initialize it for encryption
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);

        // Add AAD if provided
        if (aad != null) {
            cipher.updateAAD(aad.getBytes());
        }

        // Encrypt the plain text
        byte[] encryptedText = cipher.doFinal(plainText.getBytes());

        // Create a ByteBuffer to hold IV and encrypted text
        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encryptedText.length);
        byteBuffer.put(iv);
        byteBuffer.put(encryptedText);

        return byteBuffer.array();
    }

    public static String decrypt(byte[] encryptedData, SecretKey key, String aad) throws Exception {
        // Extract IV and encrypted text from the byte array
        ByteBuffer byteBuffer = ByteBuffer.wrap(encryptedData);
        byte[] iv = new byte[GCM_IV_LENGTH];
        byteBuffer.get(iv);
        byte[] encryptedText = new byte[byteBuffer.remaining()];
        byteBuffer.get(encryptedText);

        // Create GCMParameterSpec
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);

        // Create a Cipher instance and initialize it for decryption
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);

        // Add AAD if provided
        if (aad != null) {
            cipher.updateAAD(aad.getBytes());
        }

        // Decrypt the encrypted text
        byte[] decryptedText = cipher.doFinal(encryptedText);

        return new String(decryptedText);
    }

}
