package currycoin.script.instructions;

import currycoin.script.ByteArray;
import currycoin.script.ScriptException;
import currycoin.script.ScriptStack;

import java.nio.ByteBuffer;

public record LoadInstruction(byte numBytes, ByteArray data) implements Instruction {
	public LoadInstruction {
		if (!isInRange(numBytes)) {
			throw new IllegalArgumentException("Invalid numBytes");
		}

		if (data.length() != numBytes) {
			throw new IllegalArgumentException("Data length does not match numBytes");
		}
	}

	@Override
	public void execute(ScriptStack stack) throws ScriptException {
		stack.push(data);
	}

	@Override
	public int byteSize() {
		return numBytes + 1;
	}

	@Override
	public void apply(ByteBuffer buffer) {
		buffer.put(numBytes);
		data.apply(buffer);
	}

	public static final byte MIN_NUM_BYTES = 0;
	public static final byte MAX_NUM_BYTES = 95;

	public static boolean isInRange(byte numBytes) {
		return numBytes >= MIN_NUM_BYTES && numBytes <= MAX_NUM_BYTES;
	}

	public static LoadInstruction ofData(ByteArray data) {
		return new LoadInstruction((byte) data.length(), data);
	}

	public static LoadInstruction loadInt(int value) {
		return ofData(ByteArray.fromInt(value));
	}
}
