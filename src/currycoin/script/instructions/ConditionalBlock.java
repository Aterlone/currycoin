package currycoin.script.instructions;

import currycoin.script.ScriptStack;

import java.util.List;

public record ConditionalBlock(List<Instruction> whenTrue, List<Instruction> whenFalse) implements Instruction {
	public ConditionalBlock {
		whenTrue = List.copyOf(whenTrue);
		whenFalse = List.copyOf(whenFalse);
	}

	@Override
	public boolean execute(ScriptStack stack) {
		boolean condition = stack.pop().asBoolean();
		List<Instruction> instructions = condition ? whenTrue : whenFalse;

		for (Instruction instruction : instructions) {
			boolean result = instruction.execute(stack);
			if (!result) return false;
		}

		return true;
	}

	public static final byte IF_OPCODE = LoadInstruction.MAX_NUM_BYTES + 1;
	public static final byte NOT_IF_OPCODE = LoadInstruction.MAX_NUM_BYTES + 2;
	public static final byte ELSE_OPCODE = LoadInstruction.MAX_NUM_BYTES + 3;
	public static final byte ENDIF_OPCODE = LoadInstruction.MAX_NUM_BYTES + 4;
}
