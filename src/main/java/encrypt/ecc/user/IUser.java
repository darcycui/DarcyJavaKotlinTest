package encrypt.ecc.user;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface IUser {
    String getName();

    KeyPair getIdentityKeyPair();

    PublicKey getIdentityPublicKey();

    PrivateKey getIdentityPrivateKey();

    KeyPair getSignedPreKeyPair();

    PublicKey getSignedPreKeyPublicKey();

    PrivateKey getSignedPreKeyPrivateKey();

    KeyPair getOneTimePreKeyPair(String id);
}
