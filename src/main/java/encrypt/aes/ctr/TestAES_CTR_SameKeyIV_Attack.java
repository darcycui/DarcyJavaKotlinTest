package encrypt.aes.ctr;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HexFormat;

/**
 * 测试AES-CTR-128IV攻击
 * 密钥流重复攻击
 * 假设你加密两个消息 M1 和 M2，都使用相同的密钥 K 和相同的 IV：
 * M1 = P1 ⊕ AES(K, IV)
 * M2 = P2 ⊕ AES(K, IV)
 * 攻击者可以计算：
 * C1 ⊕ C2 = (P1 ⊕ AES(K, IV)) ⊕ (P2 ⊕ AES(K, IV)) = P1 ⊕ P2  // 密钥流被抵消了！
 * 这样知道 P1，就可以计算出 P2 了
 */
public class TestAES_CTR_SameKeyIV_Attack {
    public static void main(String[] args) throws Exception {
        System.out.println("========== AES-CTR 密钥流重复攻击演示 ==========\n");

        // 使用固定的密钥和 128 位 IV（没有计数器空间）
        byte[] key = "1234567812345678".getBytes(StandardCharsets.UTF_8);
        byte[] iv = "1234567812345678".getBytes(StandardCharsets.UTF_8);   // 128 位 IV，计数器 0 位
//        byte[] iv = CTRIVHelper.getSafeIv();   // 96位IV 32位计数器

        System.out.println("密钥 (hex): " + HexFormat.of().formatHex(key));
        System.out.println("IV (hex):  " + HexFormat.of().formatHex(iv));
        System.out.println("IV 结构：[128 位 nonce] + [0 位计数器] ❌\n");

        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // 准备两个不同的明文消息
        String message1 = "Hello, this is message 1!";
        String message2 = "Hi, this is message 2!!";

        byte[] plaintext1 = message1.getBytes(StandardCharsets.UTF_8);
        byte[] plaintext2 = message2.getBytes(StandardCharsets.UTF_8);

        System.out.println("明文 1: " + message1);
        System.out.println("明文 2: " + message2);
        System.out.println();

        // 使用相同的密钥和 IV 加密两个消息（这是错误的做法！）
        Cipher cipher1 = Cipher.getInstance("AES/CTR/NoPadding");
        cipher1.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] ciphertext1 = cipher1.doFinal(plaintext1);

        Cipher cipher2 = Cipher.getInstance("AES/CTR/NoPadding");
        cipher2.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] ciphertext2 = cipher2.doFinal(plaintext2);

        System.out.println("密文 1 (hex): " + HexFormat.of().formatHex(ciphertext1));
        System.out.println("密文 2 (hex): " + HexFormat.of().formatHex(ciphertext2));
        System.out.println();

        // 演示攻击：密钥流重复导致的漏洞
        System.out.println("========== 攻击演示 ==========\n");

        // 由于使用相同的密钥和 IV，两个密文使用的是相同的密钥流
        // C1 = P1 ⊕ KeyStream
        // C2 = P2 ⊕ KeyStream
        // C1 ⊕ C2 = P1 ⊕ P2 （密钥流被抵消！）

        byte[] xorResult = xorBytes(ciphertext1, ciphertext2);
        byte[] plaintextXor = xorBytes(plaintext1, plaintext2);

        System.out.println("C1 ⊕ C2 (hex): " + HexFormat.of().formatHex(xorResult));
        System.out.println("P1 ⊕ P2 (hex): " + HexFormat.of().formatHex(plaintextXor));
        System.out.println("两者相同：" + Arrays.equals(xorResult, plaintextXor));
        System.out.println();

        // 攻击：如果知道其中一个完整明文，可以完全解密另一个
        System.out.println("========== 已知明文攻击 ==========\n");

        // 假设攻击者完全知道明文 1
        byte[] fullRecoveredKeyStream = new byte[plaintext1.length];
        for (int i = 0; i < plaintext1.length; i++) {
            // 利用 CTR模式特性：密文 = 明文 ⊕ 密钥流
            // 推导出：密钥流 = 密文 ⊕ 明文
            fullRecoveredKeyStream[i] = (byte) (ciphertext1[i] ^ plaintext1[i]);
        }

        System.out.println("恢复的完整密钥流：" + HexFormat.of().formatHex(fullRecoveredKeyStream));

        // 使用完整的密钥流解密密文 2
        byte[] fullyDecryptedMessage2 = new byte[ciphertext2.length];
        for (int i = 0; i < ciphertext2.length; i++) {
            // 利用 CTR模式特性：密文 = 明文 ⊕ 密钥流
            // 推导出：密文 = 明文 ⊕ 密钥流
            fullyDecryptedMessage2[i] = (byte) (ciphertext2[i] ^ fullRecoveredKeyStream[i]);
        }

        System.out.println("完全解密出的明文 2: " + new String(fullyDecryptedMessage2, StandardCharsets.UTF_8));
        System.out.println("实际明文 2:         " + message2);
        System.out.println();

        System.out.println("========== 结论 ==========\n");
        System.out.println("❌ 使用 128 位 IV（计数器 0 位）会导致：");
        System.out.println("   1. 只能加密一个块，第二个块必须重用 IV");
        System.out.println("   2. 相同密钥 + 相同 IV = 相同密钥流");
        System.out.println("   3. 攻击者可以通过已知明文恢复密钥流");
        System.out.println("   4. 使用恢复的密钥流可以解密其他消息");
        System.out.println();
        System.out.println("✅ 正确做法：使用 96 位 nonce + 32 位计数器");

        // 演示正确的做法
        demonstrateCorrectApproach(key);
    }

    /**
     * 字节数组异或操作
     */
    private static byte[] xorBytes(byte[] a, byte[] b) {
        int len = Math.min(a.length, b.length);
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = (byte) (a[i] ^ b[i]);
        }
        return result;
    }

    /**
     * 演示正确的做法：96 位 nonce + 32 位计数器
     */
    private static void demonstrateCorrectApproach(byte[] key) throws Exception {
        System.out.println("\n========== 正确的做法演示 ==========\n");

        // 构造标准的 96+32 位 IV
        byte[] iv = AESCTRIVHelper.getCounterNonceIv((byte) 1);

        System.out.println("正确的 IV 结构：[96 位 nonce] + [32 位计数器]");
        System.out.println("IV (hex): " + HexFormat.of().formatHex(iv));

        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        String message1 = "Hello, this is message 1!";
        String message2 = "Hi, this is message 2!!";

        // 加密第一个消息
        Cipher cipher1 = Cipher.getInstance("AES/CTR/NoPadding");
        cipher1.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] ciphertext1 = cipher1.doFinal(message1.getBytes(StandardCharsets.UTF_8));

        // 加密第二个消息时，Java 的 CTR模式会自动递增计数器
        // 不需要手动创建新的 Cipher，同一个加密过程中计数器会自动递增
        // 但如果是新的消息，应该使用新的 nonce 或者继续递增计数器

        // 为了演示安全性，我们使用新的 nonce 加密第二个消息
        byte[] iv2 = AESCTRIVHelper.getCounterNonceIv((byte) 2);
        IvParameterSpec ivSpec2 = new IvParameterSpec(iv2);

        Cipher cipher2 = Cipher.getInstance("AES/CTR/NoPadding");
        cipher2.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec2);
        byte[] ciphertext2 = cipher2.doFinal(message2.getBytes(StandardCharsets.UTF_8));

        System.out.println("\n密文 1 (hex): " + HexFormat.of().formatHex(ciphertext1));
        System.out.println("密文 2 (hex): " + HexFormat.of().formatHex(ciphertext2));
        System.out.println("\n✅ 使用不同的计数器值，密钥流不会重复！");
        System.out.println("✅ 即使攻击者知道一个明文，也无法解密另一个消息！");
    }
}
