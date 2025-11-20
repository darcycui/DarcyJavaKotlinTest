package encrypt.sm.exchange.bean;

import encrypt.sm.exchange.SM2ExchangeHelper;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class SM2ECDHEInitiator {
    private final byte[] userId;
    private final SM2KeyPair longTermKeyPair;
    private final byte[] za;
    private SM2KeyPair ephemeralKeyPair;
    private ECPoint peerPublicKey;
    private byte[] peerZa;
    SM2ExchangeHelper helper;

    public SM2ECDHEInitiator(byte[] userId, SM2KeyPair longTermKeyPair, ECPoint peerPublicKey, SM2ExchangeHelper helper) {
        this.userId = userId;
        this.longTermKeyPair = longTermKeyPair;
        this.za = helper.calculateZA(userId, longTermKeyPair.getPublicKey());
        this.helper = helper;
        this.peerPublicKey = peerPublicKey;
    }

    /**
     * 步骤1: 生成临时密钥并准备交换数据
     */
    public InitiationResult initiateKeyExchange(byte[] peerUserId) {
        this.peerZa = helper.calculateZA(peerUserId, this.peerPublicKey);
        this.ephemeralKeyPair = helper.generateEphemeralKeyPair();

        return new InitiationResult(
                ephemeralKeyPair.getPublicKey(),
                userId,
                longTermKeyPair.getPublicKey()
        );
    }

    /**
     * 步骤2: 处理响应并计算共享密钥
     */
    public KeyExchangeResult processResponse(ResponseMessage response, int keyLength) {
        // 验证响应消息的完整性
        if (!verifyResponseMessage(response)) {
            throw new SecurityException("响应消息验证失败");
        }
        // 计算共享密钥
        byte[] sharedSecret = calculateSharedSecret(
                longTermKeyPair.getPrivateKey(),
                ephemeralKeyPair.getPrivateKey(),
                peerPublicKey,
                response.getEphemeralPublicKey()
        );
        // 密钥派生
        byte[] sessionKey = helper.kdf(sharedSecret, za, peerZa, keyLength);
        // 计算确认数据
        byte[] confirmation = helper.calculateConfirmationData(
                sharedSecret,
                ephemeralKeyPair.getPublicKey(),
                response.getEphemeralPublicKey(),
                true // 发起方
        );
        // 验证对方的确认数据
        if (!helper.verifyPeerConfirmation(response.getConfirmationData(), sharedSecret,
                ephemeralKeyPair.getPublicKey(),
                response.getEphemeralPublicKey(), false)) {
            throw new SecurityException("对方确认验证失败");
        }
        return new KeyExchangeResult(sessionKey, confirmation, sharedSecret);
    }

    /**
     * 计算共享密钥
     * 对于 Alice: tA = (dA + rA) mod n
     *            W = (PB + RB)
     *           U = tA × (PB + RB)
     */
    private byte[] calculateSharedSecret(BigInteger dA, BigInteger rA,
                                         ECPoint PB, ECPoint RB) {
        // SM2密钥交换协议的核心计算
        // tA = (dA + rA) mod n
        BigInteger z = dA.add(rA).mod(helper.domainParams.getN());

        // 计算点：W = (PB + RB)
        ECPoint w = PB.add(RB).normalize();

        // 共享点：U = tA * W
        ECPoint u = w.multiply(z).normalize();

        // 返回U的x坐标作为共享密钥基础
        return u.getAffineXCoord().getEncoded();
    }

    private boolean verifyResponseMessage(ResponseMessage response) {
        // 验证临时公钥的有效性
        if (!helper.isValidPoint(response.getEphemeralPublicKey())) {
            return false;
        }

        // 这里可以添加更多业务逻辑验证
        return true;
    }
}
