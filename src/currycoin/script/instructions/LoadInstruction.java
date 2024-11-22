package currycoin.script.instructions;

import currycoin.script.ByteArray;
import currycoin.script.ScriptStack;

public record LoadInstruction(byte numBytes, ByteArray data) implements Instruction {
	public LoadInstruction {
		if (numBytes < 0 || numBytes > 75) {
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
}
