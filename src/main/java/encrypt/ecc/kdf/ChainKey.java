package encrypt.ecc.kdf;

import encrypt.EncryptUtil;
import kotlin.Triple;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class ChainKey {

    private static final byte[] MESSAGE_KEY_SEED = {0x01};
    private static final byte[] CHAIN_KEY_SEED = {0x02};


    private final HKDF kdf;
    private final byte[] key;
    private final int index;

    public ChainKey(HKDF kdf, byte[] key, int index) {
        this.kdf = kdf;
        this.key = key;
        this.index = index;
    }

    public byte[] getKey() {
        return key;
    }

    public int getIndex() {
        return index;
    }

    public ChainKey getNextChainKey() {
        byte[] nextKey = getBaseMaterial(CHAIN_KEY_SEED);
        return new ChainKey(kdf, nextKey, index + 1);
    }

    public byte[] getMessageKeys() {
        byte[] inputKeyMaterial = getBaseMaterial(MESSAGE_KEY_SEED);
        byte[] keyMaterialBytes = kdf.deriveSecrets(inputKeyMaterial, new byte[32], "WhisperMessageKeys".getBytes(), 80);
        Triple<byte[], byte[], byte[]> triple = EncryptUtil.splitArray80(keyMaterialBytes, 32, 32, 16);
        byte[] messageKey = triple.getFirst();
        byte[] macKey = triple.getSecond();
        byte[] iv = triple.getThird();
        return messageKey;
    }

    private byte[] getBaseMaterial(byte[] seed) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key, "HmacSHA256"));

            return mac.doFinal(seed);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new AssertionError(e);
        }
    }
}
