package currycoin;

import java.nio.ByteBuffer;
import java.util.Arrays;

/// Creates a safe Hash Record which can store a hash, and return it in various forms.
public record Hash(byte[] data) {

    public Hash {
        if (data.length != 32) {
            throw new IllegalArgumentException("Must be 32 bytes long!");
        }
        data = data.clone();
    }

    public byte[] data() {
        return data.clone();
    }

    public boolean equals(Object o) {
        return o instanceof Hash h && Arrays.equals(h.data, data);
    }

    public int hashCode() {
        return Arrays.hashCode(data);
    }

    public String toString() {
        return bytesToHex(data);
    }

    private static String bytesToHex(byte[] arr) {
        StringBuilder hex = new StringBuilder();
        for (byte i : arr) {
            hex.append(String.format("%02x", i));
        }
        return hex.toString();
    }

    public static Hash parseFrom(ByteBuffer buffer) {
        byte[] data = new byte[32];
        buffer.get(data);
        return new Hash(data);
    }

    public int byteSize() {
        return 32;
    }

    public void apply(ByteBuffer buffer) {
        buffer.put(data);
    }

    private static final Hash EMPTY = new Hash(new byte[32]);

    public static Hash empty() {
        return EMPTY;
    }
}
