package encrypt.sm.exchange.bean;

import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class SM2KeyPair {
    private final BigInteger privateKey;
    private final ECPoint publicKey;

    public SM2KeyPair(BigInteger privateKey, ECPoint publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public BigInteger getPrivateKey() { return privateKey; }
    public ECPoint getPublicKey() { return publicKey; }
}
