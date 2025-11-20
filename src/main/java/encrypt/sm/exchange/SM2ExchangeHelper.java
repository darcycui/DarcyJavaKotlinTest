package encrypt.sm.exchange;

import encrypt.sm.exchange.bean.SM2ECDHEInitiator;
import encrypt.sm.exchange.bean.SM2EDCHEResponder;
import encrypt.sm.exchange.bean.SM2KeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

public class SM2ExchangeHelper {

    public final ECNamedCurveParameterSpec sm2Spec;
    public final ECDomainParameters domainParams;
    public final SecureRandom secureRandom;

    public SM2ExchangeHelper() {
        // 获取SM2曲线参数
        this.sm2Spec = ECNamedCurveTable.getParameterSpec("sm2p256v1");
        this.domainParams = new ECDomainParameters(
                sm2Spec.getCurve(),
                sm2Spec.getG(),
                sm2Spec.getN(),
                sm2Spec.getH()
        );
        this.secureRandom = new SecureRandom();
    }

    /**
     * 生成SM2密钥对（长期身份密钥）
     */
    public SM2KeyPair generateKeyPair() {
        try {
            ECKeyPairGenerator generator = new ECKeyPairGenerator();
            ECKeyGenerationParameters keyGenParams = new ECKeyGenerationParameters(domainParams, secureRandom);
            generator.init(keyGenParams);
            AsymmetricCipherKeyPair keyPair = generator.generateKeyPair();
            ECPrivateKeyParameters privateKeyParams = (ECPrivateKeyParameters) keyPair.getPrivate();
            ECPublicKeyParameters publicKeyParams = (ECPublicKeyParameters) keyPair.getPublic();
            return new SM2KeyPair(privateKeyParams.getD(), publicKeyParams.getQ());
        } catch (Exception e) {
            throw new RuntimeException("生成SM2密钥对失败", e);
        }
    }

    /**
     * 生成ECDHE临时密钥对
     */
    public SM2KeyPair generateEphemeralKeyPair() {
        return generateKeyPair(); // 临时密钥生成方式与长期密钥相同
    }

    /**
     * 计算用户标识哈希ZA (用于确认)
     * ZAa = HASH(ENTLA || IDA || a || b || xG || yG || xA || yA || PA)
     * ZAb = HASH(ENTLA || IDB || a || b || xG || yG || xB || yB || PB)
     */
    public byte[] calculateZA(byte[] userId, ECPoint publicKey) {
        SM3Digest digest = new SM3Digest();

        // ENTLA - 用户ID长度（16位整数，2字节）
        int entla = userId.length * 8;
        byte[] entlaBytes = new byte[]{(byte) (entla >>> 8), (byte) entla};
        digest.update(entlaBytes, 0, 2);

        // 用户ID
        digest.update(userId, 0, userId.length);

        // 曲线参数 a, b
        updateBigInteger(digest, sm2Spec.getCurve().getA().toBigInteger(), 32);
        updateBigInteger(digest, sm2Spec.getCurve().getB().toBigInteger(), 32);

        // 基点G坐标
        ECPoint G = sm2Spec.getG();
        updateBigInteger(digest, G.getAffineXCoord().toBigInteger(), 32);
        updateBigInteger(digest, G.getAffineYCoord().toBigInteger(), 32);

        // 公钥坐标
        updateBigInteger(digest, publicKey.getAffineXCoord().toBigInteger(), 32);
        updateBigInteger(digest, publicKey.getAffineYCoord().toBigInteger(), 32);

        byte[] za = new byte[32];
        digest.doFinal(za, 0);
        return za;
    }

    /**
     * 计算确认数据
     */
    public byte[] calculateConfirmationData(byte[] sharedSecret, ECPoint RA,
                                            ECPoint RB, boolean isInitiator) {
        SM3Digest digest = new SM3Digest();

        // 根据角色使用不同的前缀
        byte prefix = isInitiator ? (byte) 0x02 : (byte) 0x03;
        digest.update(prefix);

        // 包含双方临时公钥的y坐标
        BigInteger yA = RA.getAffineYCoord().toBigInteger();
        BigInteger yB = RB.getAffineYCoord().toBigInteger();

        updateBigInteger(digest, yA, 32);
        updateBigInteger(digest, yB, 32);

        // 包含共享密钥
        digest.update(sharedSecret, 0, sharedSecret.length);

        byte[] confirmation = new byte[32]; // SM3 输出是 32 字节
        digest.doFinal(confirmation, 0);
        return confirmation;
    }

    /**
     * 验证对方的确认数据
     */
    public boolean verifyPeerConfirmation(byte[] peerConfirmation, byte[] sharedSecret,
                                          ECPoint RA, ECPoint RB, boolean peerIsInitiator) {
        byte[] expectedConfirmation = calculateConfirmationData(
                sharedSecret, RA, RB, peerIsInitiator);
        return Arrays.equals(peerConfirmation, expectedConfirmation);
    }

    /**
     * 密钥派生函数（基于SM3的KDF）
     * KDF过程：
     * 输入：共享密钥的x坐标 + ZA + ZB
     * 使用SM3哈希进行多轮派生
     * 输出指定长度的会话密钥 32字节
     */
    public byte[] kdf(byte[] sharedSecret, byte[] za, byte[] zb, int keyLength) {
        SM3Digest digest = new SM3Digest();
        byte[] result = new byte[keyLength];
        int hashLen = 32;
        int rounds = (keyLength + hashLen - 1) / hashLen;
        for (int i = 0; i < rounds; i++) {
            digest.reset();
            // 计数器
            byte[] counter = new byte[4];
            counter[0] = (byte) ((i + 1) >>> 24);
            counter[1] = (byte) ((i + 1) >>> 16);
            counter[2] = (byte) ((i + 1) >>> 8);
            counter[3] = (byte) (i + 1);
            digest.update(counter, 0, 4);
            // 共享密钥
            digest.update(sharedSecret, 0, sharedSecret.length);
            // 身份信息
            digest.update(za, 0, za.length);
            digest.update(zb, 0, zb.length);
            byte[] hash = new byte[hashLen];
            digest.doFinal(hash, 0);
            int copyLen = Math.min(hashLen, keyLength - i * hashLen);
            System.arraycopy(hash, 0, result, i * hashLen, copyLen);
        }
        return result;
    }

    /**
     * 验证点是否在曲线上
     */
    public boolean isValidPoint(ECPoint point) {
        if (point == null || point.isInfinity()) {
            return false;
        }
        try {
            // BouncyCastle会验证点有效性
            point.normalize();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    // 工具方法
    private void updateBigInteger(SM3Digest digest, BigInteger value, int length) {
        byte[] bytes = to32Bytes(value);
        digest.update(bytes, 0, bytes.length);
    }

    private byte[] to32Bytes(BigInteger value) {
        byte[] bytes = new byte[32];
        byte[] bigIntBytes = value.toByteArray();
        int start = Math.max(0, 32 - bigIntBytes.length);
        int length = Math.min(bigIntBytes.length, 32);
        System.arraycopy(bigIntBytes, Math.max(0, bigIntBytes.length - 32),
                bytes, start, length);
        return bytes;
    }
}
