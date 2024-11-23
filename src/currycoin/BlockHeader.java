package currycoin;

import java.security.*;

/// The header of a block.
public record BlockHeader(Hash prevHash, Hash transHash, int nonce) {

    public Hash hash() throws NoSuchAlgorithmException {
        MessageDigest mess = MessageDigest.getInstance("SHA-256");
        mess.update(prevHash.data());
        mess.update(transHash.data());

        for (int i = 0; i < 4; i++) {
            mess.update((byte) (nonce >> (i*8)));
        }
        return new Hash(mess.digest());
    }
}