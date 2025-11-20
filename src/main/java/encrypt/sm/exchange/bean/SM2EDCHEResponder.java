package encrypt.sm.exchange.bean;

import encrypt.sm.exchange.SM2ExchangeHelper;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class SM2EDCHEResponder {
    private final byte[] userId;
    private final SM2KeyPair longTermKeyPair;
    private final byte[] za;
    private SM2KeyPair ephemeralKeyPair;
    private ECPoint peerPublicKey;
    private byte[] peerZa;
    SM2ExchangeHelper helper;

    public SM2EDCHEResponder(byte[] userId, SM2KeyPair longTermKeyPair, ECPoint peerPublicKey, SM2ExchangeHelper helper) {
        this.userId = userId;
        this.longTermKeyPair = longTermKeyPair;
        this.za = helper.calculateZA(userId, longTermKeyPair.getPublicKey());
        this.helper = helper;
        this.peerPublicKey = peerPublicKey;
    }

    /**
     * 处理发起方的请求并生成响应
     */
    public ResponseMessage processInitiation(InitiationMessage initiation, int keyLength) {
        this.peerZa = helper.calculateZA(initiation.getUserId(), peerPublicKey);
        this.ephemeralKeyPair = helper.generateEphemeralKeyPair();

        // 计算共享密钥
        byte[] sharedSecret = calculateSharedSecret(
                longTermKeyPair.getPrivateKey(),
                ephemeralKeyPair.getPrivateKey(),
                peerPublicKey,
                initiation.getEphemeralPublicKey()
        );

        // 密钥派生(KDF)
        byte[] sessionKey = helper.kdf(sharedSecret, peerZa, za, keyLength);

        // 计算确认数据
        byte[] confirmation = helper.calculateConfirmationData(
                sharedSecret,
                initiation.getEphemeralPublicKey(),
                ephemeralKeyPair.getPublicKey(),
                false // 响应方
        );

        return new ResponseMessage(ephemeralKeyPair.getPublicKey(), confirmation, sessionKey);
    }

    /**
     * 验证发起方的确认消息
     */
    public boolean verifyInitiatorConfirmation(byte[] peerConfirmation,
                                               ECPoint peerEphemeralPublicKey,
                                               int keyLength) {
        // 重新计算共享密钥（与processInitiation中相同）
        byte[] sharedSecret = calculateSharedSecret(
                longTermKeyPair.getPrivateKey(),
                ephemeralKeyPair.getPrivateKey(),
                peerPublicKey,
                peerEphemeralPublicKey
        );

        return helper.verifyPeerConfirmation(peerConfirmation, sharedSecret,
                peerEphemeralPublicKey,
                ephemeralKeyPair.getPublicKey(), true);
    }

    /**
     * 计算共享密钥
     * 对于 Bob:   tB = (dB + rB) mod n
     *            W = (PA + RA)
     *           V = tB × (PA + RA)
     */
    private byte[] calculateSharedSecret(BigInteger dB, BigInteger rB,
                                         ECPoint PA, ECPoint RA) {
        // 响应方的共享密钥计算与发起方对称
        // tB = (dB + rB) mod n
        BigInteger z = dB.add(rB).mod(helper.domainParams.getN());
        // W = (PA + RA)
        ECPoint w = PA.add(RA).normalize();
        // V = tB × (PA + RA)
        ECPoint v = w.multiply(z).normalize();
        return v.getAffineXCoord().getEncoded();
    }
}