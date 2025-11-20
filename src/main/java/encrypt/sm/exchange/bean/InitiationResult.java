package encrypt.sm.exchange.bean;

import org.bouncycastle.math.ec.ECPoint;

public class InitiationResult {
    private final ECPoint ephemeralPublicKey;
    private final byte[] userId;
    private final ECPoint longTermPublicKey;

    public InitiationResult(ECPoint ephemeralPublicKey, byte[] userId, ECPoint longTermPublicKey) {
        this.ephemeralPublicKey = ephemeralPublicKey;
        this.userId = userId;
        this.longTermPublicKey = longTermPublicKey;
    }

    public ECPoint getEphemeralPublicKey() {
        return ephemeralPublicKey;
    }

    public byte[] getUserId() {
        return userId;
    }

    public ECPoint getLongTermPublicKey() {
        return longTermPublicKey;
    }
}
