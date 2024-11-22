package currycoin.script.instructions;

import currycoin.script.Instruction;
import currycoin.script.ScriptStack;

import java.nio.ByteBuffer;

public class PushInstruction implements Instruction {
    private ByteBuffer data;

    public PushInstruction(ByteBuffer data) {
        this.data = data;
    }

    public void setData(ByteBuffer data) {
        this.data = data;
    }

    @Override
    public void execute(ScriptStack stack) {
        stack.push(this.data);
    }
}
