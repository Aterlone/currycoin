package currycoin.script.instructions;

import currycoin.script.ByteArray;
import currycoin.script.ScriptStack;

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
	public boolean execute(ScriptStack stack) {
		stack.push(data);
		return true;
	}

	public static final byte MIN_NUM_BYTES = 0;
	public static final byte MAX_NUM_BYTES = 75;

	public static boolean isInRange(byte numBytes) {
		return numBytes >= MIN_NUM_BYTES && numBytes <= MAX_NUM_BYTES;
	}
}
