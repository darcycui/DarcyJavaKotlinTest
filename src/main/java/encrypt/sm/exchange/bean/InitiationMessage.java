package encrypt.sm.exchange.bean;

import org.bouncycastle.math.ec.ECPoint;

public class InitiationMessage extends InitiationResult {
    public InitiationMessage(ECPoint ephemeralPublicKey, byte[] userId, ECPoint longTermPublicKey) {
        super(ephemeralPublicKey, userId, longTermPublicKey);
    }
}
