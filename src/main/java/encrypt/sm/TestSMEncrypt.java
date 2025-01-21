package encrypt.sm;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class TestSMEncrypt {
    static {
        // 添加三方加密库实现
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) {
        testSM4();
    }

    private static void testSM4() {
        String plainText = "hello world";
        System.out.println("plainText:" + plainText);
        SM4Util sm4 = new SM4Util();
        String cipherText = sm4.encrypt(plainText);
        System.out.println("cipherText=" + cipherText);
        String plainText2 = sm4.decrypt(cipherText);
        System.out.println("plainText2=" + plainText2);
    }
}
