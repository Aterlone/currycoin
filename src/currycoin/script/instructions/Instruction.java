package currycoin.script.instructions;

import currycoin.script.ScriptStack;

public sealed interface Instruction permits ConditionalBlock, LoadInstruction, OrdinaryInstruction {
    boolean execute(ScriptStack stack);
}
