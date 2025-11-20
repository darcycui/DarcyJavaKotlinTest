package encrypt.sm.exchange.bean;

public class KeyExchangeResult {
    private final byte[] sessionKey;
    private final byte[] confirmationData;
    private final byte[] sharedSecret;

    public KeyExchangeResult(byte[] sessionKey, byte[] confirmationData, byte[] sharedSecret) {
        this.sessionKey = sessionKey;
        this.confirmationData = confirmationData;
        this.sharedSecret = sharedSecret;
    }

    public byte[] getSessionKey() { return sessionKey; }
    public byte[] getConfirmationData() { return confirmationData; }
    public byte[] getSharedSecret() { return sharedSecret; }
}
