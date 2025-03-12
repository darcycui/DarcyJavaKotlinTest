package encrypt.mac;

import encrypt.EncryptUtil;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.macs.CMac;
import org.bouncycastle.crypto.params.KeyParameter;

public class TestCMAC {
    public static void main(String[] args) {
        // 需要使用bouncycastle库
        // 密钥（16字节，AES-128）
//        byte[] key = "1234567890123456".getBytes();
        // 密钥（32字节，AES-256）
        byte[] key = "12345678901234561234567890123456".getBytes();
        // 待认证的数据
        byte[] data = "Hello, CMAC!".getBytes();
        // 计算CMAC 长度128位(AES-CBC分组 块大小)
        // 915757a54f0aba03994a0c0bce638d34
        // 193f03132e8af484d5ad7f68949c7728
        String cmac = calculateCMAC(key, data);
        // 输出结果
        System.out.println("CMAC: " + cmac);
    }

    public static String calculateCMAC(byte[] key, byte[] data) {
        try {
            CMac cMac = new CMac(new AESEngine(), 128); // 使用AES引擎，128位MAC
            cMac.init(new KeyParameter(key));
            cMac.update(data, 0, data.length);
            byte[] mac = new byte[cMac.getMacSize()];
            cMac.doFinal(mac, 0);
            return EncryptUtil.bytesToHex(mac);
        } catch (Exception e) {
            System.err.println("计算CMAC失败:" + e.getMessage());
            e.printStackTrace();
        }
        return "";
    }
}
