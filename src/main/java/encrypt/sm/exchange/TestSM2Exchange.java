package encrypt.sm.exchange;

import encrypt.sm.exchange.bean.*;
import org.bouncycastle.util.encoders.Hex;

import java.util.Arrays;

public class TestSM2Exchange {
    /* 符号说明
     * 长期密钥 dA(私钥) PA(公钥)
     * 临时密钥 rA(私钥) RA(公钥)
     * 用户标识 IDA
     * 用户标识哈希 ZA
     * 发送方确认数据 S1
     * 响应方确认数据 S2
     * 会话密钥 SessionKey
     * */

    /* 消息交换序列
  前提: PA,PB 已通过其他可信渠道交换 不在密钥协商中传递
  Alice                            Bob
  | -------- Initiation ---------> |
  | RA, IDA                        |
  |                                |
  | <------- Response ------------ |
  | RB, S1                         |
  |                                |
  | ------- Confirmation --------> |
  | S2                             |
  |                                |
  | <------- Final Verify ---------|
  */
    public static void main(String[] args) {
        System.out.println("SM2实现ECDHE密钥协商");
        int keyLength = 32; // 32字节会话密钥

        // 1. 双方生成长期身份密钥对
        SM2ExchangeHelper helper = new SM2ExchangeHelper();
        SM2KeyPair aliceLongTermKey = helper.generateKeyPair();
        System.out.println("Alice长期公钥: " + aliceLongTermKey.getPublicKey().getAffineXCoord().toString().substring(0, 16) + "...");
        SM2KeyPair bobLongTermKey = helper.generateKeyPair();
        System.out.println("Bob长期公钥: " + bobLongTermKey.getPublicKey().getAffineXCoord().toString().substring(0, 16) + "...");

        // 2. 用户标识(用于计算ZA)
        byte[] aliceUserId = "alice@example.com".getBytes();
        byte[] bobUserId = "bob@example.com".getBytes();

        // 前提: 长期身份公钥(PA PB) 已通过其他可信渠道交换 不在密钥协商中传递

        // 3. 创建双方实例 (这时Alice已拥有Bob的长期身份密钥 Bob已拥有Alice的长期身份密钥)
        SM2ECDHEInitiator alice = new SM2ECDHEInitiator(aliceUserId, aliceLongTermKey, bobLongTermKey.getPublicKey(), new SM2ExchangeHelper());
        SM2EDCHEResponder bob = new SM2EDCHEResponder(bobUserId, bobLongTermKey, aliceLongTermKey.getPublicKey(), new SM2ExchangeHelper());


        // 4. alice发起密钥交换: 生成临时密钥并发送数据(RA,IDA)
        System.out.println("\n步骤1: Alice发起密钥交换...");
        // 传入: bob 的长期公钥 和 userId
        InitiationResult initiation = alice.initiateKeyExchange(bobUserId);
        InitiationMessage initiationMessage = new InitiationMessage(
                initiation.getEphemeralPublicKey(),
                initiation.getUserId(),
                initiation.getLongTermPublicKey()
        );
        System.out.println("Alice临时公钥: " + initiation.getEphemeralPublicKey().getAffineXCoord().toString().substring(0, 16) + "...");

        // 5. bob 生成临时密钥对 计算确认数据S1 计算共享密钥SessionKey 发送数据(RB, S1)
        System.out.println("\n步骤2: Bob处理请求并响应");
        ResponseMessage bobResponseMessage = bob.processInitiation(initiationMessage, keyLength); // 32字节会话密钥
        System.out.println("Bob临时公钥: " + bobResponseMessage.getEphemeralPublicKey().getAffineXCoord().toString().substring(0, 16) + "...");
        System.out.println("Bob确认数据S1: " + Hex.toHexString(bobResponseMessage.getConfirmationData()).substring(0, 16) + "...");
        System.out.println("Bob会话密钥: " + Hex.toHexString(bobResponseMessage.getSessionKey()).substring(0, 16) + "...");

        // 6. Alice验证Bob的确认数据S1 计算会话密钥SessionKey 发送数据(S2)
        System.out.println("\n步骤3: Alice验证Bob的确认数据");
        KeyExchangeResult aliceResult = alice.processResponse(bobResponseMessage, keyLength);
        System.out.println("Alice确认数据S2: " + Hex.toHexString(aliceResult.getConfirmationData()).substring(0, 16) + "...");
        System.out.println("Alice会话密钥: " + Hex.toHexString(aliceResult.getSessionKey()).substring(0, 16) + "...");

        // 7. Bob验证Alice的确认数据S2
        System.out.println("\n步骤4: Bob验证Alice的确认数据");
        boolean verificationResult = bob.verifyInitiatorConfirmation(aliceResult.getConfirmationData(), initiation.getEphemeralPublicKey(), keyLength);
        System.out.println("确认验证结果: " + (verificationResult ? "成功" : "失败"));

        // 8. 最终验证
        System.out.println("\n=== 最终验证结果 ===");
        boolean keysMatch = Arrays.equals(
                aliceResult.getSessionKey(),
                bobResponseMessage.getSessionKey()
        );
        System.out.println("会话密钥一致性: " + (keysMatch ? "成功" : "失败"));
        System.out.println("密钥交换协议: " + (keysMatch && verificationResult ? "成功" : "存在异常"));

        if (keysMatch && verificationResult) {
            System.out.println("\n✅ SM2-ECDHE密钥交换协议执行成功！");
            System.out.println("共享会话密钥: " + Hex.toHexString(aliceResult.getSessionKey()));
        } else {
            System.out.println("\n❌ SM2-ECDHE密钥交换协议执行失败！");
        }
    }
}
