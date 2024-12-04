package currycoin.script;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Arrays;

public record ByteArray(byte[] data) {
	public ByteArray {
		data = data.clone();
	}

	@Override
	public byte[] data() {
		return data.clone();
	}

	public int length() {
		return data.length;
	}

	public boolean asBoolean() {
		for (byte item : data) {
			if (item != 0)
				return true;
		}

		return false;
	}

	public boolean isInt() {
		return data.length <= 4;
	}

	public int toInt() {
		if (data.length > 4) {
			throw new ScriptException.VerificationException("Data is too long to be an integer");
		}

		if (data.length == 0) {
			return 0;
		}

		// little endian
		int value = 0;
		for (int i = 0; i < data.length; i++) {
			value |= (data[i] & 0xFF) << (i * 8);
		}

		return value;
	}

	public boolean isLong() {
		return data.length <= 8;
	}

	public long toLong() {
		if (data.length > 8) {
			throw new ScriptException.VerificationException("Data is too long to be a long");
		}

		if (data.length == 0) {
			return 0;
		}

		// little endian
		long value = 0;
		for (int i = 0; i < data.length; i++) {
			value |= ((long) (data[i] & 0xFF)) << (i * 8);
		}

		return value;
	}

	private static final ByteArray ZERO = new ByteArray(new byte[0]);

	public static ByteArray fromInt(int value) {
		if (value == 0) return ZERO;

		int size = 4 - Integer.numberOfLeadingZeros(value) / 8;
		byte[] data = new byte[size];
		for (int i = 0; i < size; i++) {
			data[i] = (byte) (value >> (i * 8));
		}

		return new ByteArray(data);
	}

	public static ByteArray fromLong(long value) {
		if (value == 0) return ZERO;

		int size = 8 - Long.numberOfLeadingZeros(value) / 8;
		byte[] data = new byte[size];
		for (int i = 0; i < size; i++) {
			data[i] = (byte) (value >> (i * 8));
		}

		return new ByteArray(data);
	}

	public static ByteArray empty() {
		return ZERO;
	}

	public void apply(ByteBuffer buffer) {
		buffer.put(data);
	}

	public void addToDigest(MessageDigest digest) {
		digest.update(data);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(data);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof ByteArray h && Arrays.equals(h.data, data);
	}

	@Override
	public String toString() {
		return "0x" + bytesToHex(data);
	}

	public String toPrettyString() {
		String string = bytesToHex(data);
		if (isInt())
			return String.format("0x%-8s (%d)", string, toInt());
		else
			return "0x" + string;
	}

	private static String bytesToHex(byte[] arr) {
		StringBuilder hex = new StringBuilder();
		for (byte i : arr) {
			hex.append(String.format("%02x", i));
		}
		return hex.toString();
	}
}
