package currycoin.script;

import currycoin.script.instructions.Instruction;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public record Script(List<Instruction> instructions) {
	public Script {
		instructions = List.copyOf(instructions);
	}

	public void execute(ScriptStack stack) throws ScriptException {
		for (Instruction instruction : instructions) {
			instruction.execute(stack);
		}
	}

	public static Script parseFrom(ByteBuffer data) {
		List<Instruction> instructions = new ArrayList<>();
		while (data.hasRemaining()) {
			instructions.add(Instruction.parseFrom(data));
		}
		return new Script(instructions);
	}

	public static Script of(Instruction... instructions) {
		return new Script(List.of(instructions));
	}

	public int byteSize() {
		return instructions.stream().mapToInt(Instruction::byteSize).sum();
	}

	public void apply(ByteBuffer buffer) {
		for (Instruction instruction : instructions) {
			instruction.apply(buffer);
		}
	}
}
