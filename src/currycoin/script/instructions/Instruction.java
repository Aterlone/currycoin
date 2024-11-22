package currycoin.script.instructions;

import currycoin.script.ByteArray;
import currycoin.script.ScriptStack;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public sealed interface Instruction permits LoadInstruction, ConditionalBlock, OrdinaryInstruction {
    boolean execute(ScriptStack stack);

    static Instruction parseFrom(ByteBuffer buffer) {
        return parseFrom0(buffer, 0);
    }

    private static Instruction parseFrom0(ByteBuffer buffer, int depth) {
        if (depth > 20) {
            throw new IllegalArgumentException("Maximum parsing stack depth exceeded!");
        }
        byte opcode = buffer.get();
        if (LoadInstruction.isInRange(opcode)) {
            byte[] data = new byte[opcode];
            buffer.get(data);
            return new LoadInstruction(opcode, new ByteArray(data));
        } else if (opcode == ConditionalBlock.IF_OPCODE) {
            List<Instruction> whenTrue = new ArrayList<>();
            Instruction instruction;
            while ((instruction = parseFrom0(buffer, depth + 1)) != null) {
                whenTrue.add(instruction);
            }

            buffer.mark();
            if (buffer.get() != ConditionalBlock.ELSE_OPCODE) {
                buffer.reset();
                return new ConditionalBlock(whenTrue, List.of());
            } else {
                List<Instruction> whenFalse = new ArrayList<>();
                while ((instruction = parseFrom0(buffer, depth + 1)) != null) {
                    whenFalse.add(instruction);
                }
                return new ConditionalBlock(whenTrue, whenFalse);
            }
        } else if (opcode == ConditionalBlock.NOT_IF_OPCODE) {
            List<Instruction> whenFalse = new ArrayList<>();
            Instruction instruction;
            while ((instruction = parseFrom0(buffer, depth + 1)) != null) {
                whenFalse.add(instruction);
            }

            buffer.mark();
            if (buffer.get() != ConditionalBlock.ELSE_OPCODE) {
                buffer.reset();
                return new ConditionalBlock(List.of(), whenFalse);
            } else {
                List<Instruction> whenTrue = new ArrayList<>();
                while ((instruction = parseFrom0(buffer, depth + 1)) != null) {
                    whenFalse.add(instruction);
                }
                return new ConditionalBlock(whenTrue, whenFalse);
            }
        } else if (opcode == ConditionalBlock.ELSE_OPCODE) {
            throw new IllegalArgumentException("Unexpected ELSE opcode");
        } else if (opcode == ConditionalBlock.ENDIF_OPCODE) {
            if (depth == 0)
                throw new IllegalArgumentException("Unexpected ENDIF opcode");
            return null;
        } else {
            // must be an ordinary instruction
            int ordinal = opcode - OrdinaryInstruction.FIRST_OPCODE;
            return OrdinaryInstruction.values()[ordinal];
        }
    }
}
