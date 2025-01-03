package currycoin.script.instructions;

import currycoin.script.ScriptException;
import currycoin.script.ScriptStack;

import java.nio.ByteBuffer;
import java.util.List;

public record ConditionalBlock(List<Instruction> whenTrue, List<Instruction> whenFalse) implements Instruction {
	public ConditionalBlock {
		whenTrue = List.copyOf(whenTrue);
		whenFalse = List.copyOf(whenFalse);
	}

	@Override
	public void execute(ScriptStack stack) throws ScriptException {
		boolean condition = stack.pop().asBoolean();
		List<Instruction> instructions = condition ? whenTrue : whenFalse;

		for (Instruction instruction : instructions) {
			instruction.execute(stack);
		}
	}

	@Override
	public int byteSize() {
		if (whenFalse.isEmpty()) {
			return whenTrue.stream().mapToInt(Instruction::byteSize).sum() + 2;
		} else if (whenTrue.isEmpty()) {
			return whenFalse.stream().mapToInt(Instruction::byteSize).sum() + 2;
		} else {
			return whenTrue.stream().mapToInt(Instruction::byteSize).sum()
					+ whenFalse.stream().mapToInt(Instruction::byteSize).sum() + 4;
		}
	}

	@Override
	public void apply(ByteBuffer buffer) {
		if (whenFalse.isEmpty()) {
			buffer.put(IF_OPCODE);
			for (Instruction instruction : whenTrue) {
				instruction.apply(buffer);
			}
			buffer.put(ENDIF_OPCODE);
		} else if (whenTrue.isEmpty()) {
			buffer.put(NOT_IF_OPCODE);
			for (Instruction instruction : whenFalse) {
				instruction.apply(buffer);
			}
			buffer.put(ENDIF_OPCODE);
		} else {
			buffer.put(IF_OPCODE);
			for (Instruction instruction : whenTrue) {
				instruction.apply(buffer);
			}
			buffer.put(ENDIF_OPCODE);
			buffer.put(ELSE_OPCODE);
			for (Instruction instruction : whenFalse) {
				instruction.apply(buffer);
			}
			buffer.put(ENDIF_OPCODE);
		}
	}

	public static ConditionalBlock whenTrue(Instruction... whenTrue) {
		return new ConditionalBlock(List.of(whenTrue), List.of());
	}

	public static ConditionalBlock whenFalse(Instruction... whenFalse) {
		return new ConditionalBlock(List.of(), List.of(whenFalse));
	}

	public static final byte IF_OPCODE = LoadInstruction.MAX_NUM_BYTES + 1;
	public static final byte NOT_IF_OPCODE = LoadInstruction.MAX_NUM_BYTES + 2;
	public static final byte ELSE_OPCODE = LoadInstruction.MAX_NUM_BYTES + 3;
	public static final byte ENDIF_OPCODE = LoadInstruction.MAX_NUM_BYTES + 4;
}
