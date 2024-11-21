package block;

import javax.crypto.*;
import java.security.*;
import java.util.*;

public record Block (byte[] prevHash, byte[] transHash, int nonce) {

    public byte[] hash() throws NoSuchAlgorithmException {
        MessageDigest mess = MessageDigest.getInstance("SHA-256");
        mess.update(prevHash);
        mess.update(transHash);

        for (int i = 0; i < 4; ++i) {
            mess.update((byte) (nonce >> (i*8)));
        }
        return mess.digest();
    }
}