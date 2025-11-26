package encrypt.ecc.user;

import encrypt.ecc.exchange.ECCExchangeHelper;

import java.security.*;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseUser implements IUser {

    private final KeyPair identityKeyPair;
    private final KeyPair signedPreKeyPair;
    private final Map<String, KeyPair> oneTimePreKeyPairMap;

    public BaseUser() {
        this.identityKeyPair = ECCExchangeHelper.generateKeyPair();
        this.signedPreKeyPair = ECCExchangeHelper.generateKeyPair();
        this.oneTimePreKeyPairMap = new HashMap<>(100);
        for (int i = 0; i < 100; i++) {
            KeyPair item = ECCExchangeHelper.generateKeyPair();
            oneTimePreKeyPairMap.put(String.valueOf(i + 1), item);
        }
    }

    @Override
    public KeyPair getIdentityKeyPair() {
        return identityKeyPair;
    }

    @Override
    public PublicKey getIdentityPublicKey() {
        return identityKeyPair.getPublic();
    }

    @Override
    public PrivateKey getIdentityPrivateKey() {
        return identityKeyPair.getPrivate();
    }

    @Override
    public KeyPair getSignedPreKeyPair() {
        return signedPreKeyPair;
    }

    @Override
    public PublicKey getSignedPreKeyPublicKey() {
        return signedPreKeyPair.getPublic();
    }

    @Override
    public PrivateKey getSignedPreKeyPrivateKey() {
        return signedPreKeyPair.getPrivate();
    }

    @Override
    public KeyPair getOneTimePreKeyPair(String id) {
        if (!oneTimePreKeyPairMap.containsKey(id)) {
            throw new RuntimeException("OneTimePreKeyPair 不存在:" + id);
        }
        return oneTimePreKeyPairMap.get(id);
    }
}
