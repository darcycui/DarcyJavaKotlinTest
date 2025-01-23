package encrypt.dh;

import java.security.KeyPair;

public class TestDHExchange {
    public static void main(String[] args) {
//        testDH();
        testECDH();
    }

    private static void testDH() {
        try {
            KeyPair keyPairA = DHUtil.generateKeyPair();
            KeyPair keyPairB = DHUtil.generateKeyPair();
            DHUtil.exchange(keyPairA, keyPairB);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testECDH() {
        try {
            KeyPair keyPairA = ECDHUtil.generateKeyPair();
            KeyPair keyPairB = ECDHUtil.generateKeyPair();
            ECDHUtil.exchange(keyPairA, keyPairB);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
