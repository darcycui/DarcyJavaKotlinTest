package encrypt.aes.ctr;

import encrypt.EncryptUtil;

import java.nio.charset.StandardCharsets;

public class AESCTRIVHelper {
    /**
     * 模拟 128 位 IV（96 位 nonce + 32 位计数器）
     * 注意这里不能直接用四个0填充IV  因为字符 '0' 的 ASCII 码为 0x30，不是 0x00
     */
    public static byte[] getCounterNonceIv(byte counter_initial_value) {
        byte[] nonce = "123456781234".getBytes(StandardCharsets.UTF_8);  // 96位，计数器 0 位
        byte[] initialCounter = new byte[4];  // 32位
        initialCounter[3] = counter_initial_value;  // 计数器从0开始（大端）

        byte[] iv128 = new byte[16];
        System.arraycopy(nonce, 0, iv128, 0, 12);
        System.arraycopy(initialCounter, 0, iv128, 12, 4);
        System.out.println("iv128:" + EncryptUtil.toHexString(iv128));
        return iv128;
    }
}
