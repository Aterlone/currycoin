package currycoin.script;

import currycoin.script.instructions.Instruction;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public record Script(List<Instruction> instructions) {
	public Script {
		instructions = List.copyOf(instructions);
	}

	public boolean execute(ScriptStack stack) {
		for (Instruction instruction : instructions) {
			boolean result = instruction.execute(stack);
			if (!result) return false;
		}

		return true;
	}

	public static Script parseFrom(ByteBuffer data) {
		List<Instruction> instructions = new ArrayList<>();
		while (data.hasRemaining()) {
			instructions.add(Instruction.parseFrom(data));
		}
		return new Script(instructions);
	}
}
