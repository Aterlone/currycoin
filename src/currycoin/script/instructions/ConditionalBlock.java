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
}
