package currycoin.script.instructions;

import currycoin.script.ByteArray;
import currycoin.script.Instruction;
import currycoin.script.ScriptStack;


public class PushInstruction implements Instruction {
    private ByteArray data;

    public PushInstruction(ByteArray data) {
        this.data = data;
    }

    public void setData(ByteArray data) {
        this.data = data;
    }

    @Override
    public void execute(ScriptStack stack) {
        //skip for now
    }
}
