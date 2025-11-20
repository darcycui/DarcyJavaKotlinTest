package encrypt.sm.exchange.bean;

import org.bouncycastle.math.ec.ECPoint;

public class ResponseMessage {
    private final ECPoint ephemeralPublicKey;
    private final byte[] confirmationData;
    private final byte[] sessionKey;

    public ResponseMessage(ECPoint ephemeralPublicKey, byte[] confirmationData, byte[] sessionKey) {
        this.ephemeralPublicKey = ephemeralPublicKey;
        this.confirmationData = confirmationData;
        this.sessionKey = sessionKey;
    }

    public ECPoint getEphemeralPublicKey() { return ephemeralPublicKey; }
    public byte[] getConfirmationData() { return confirmationData; }
    public byte[] getSessionKey() { return sessionKey; }
}
