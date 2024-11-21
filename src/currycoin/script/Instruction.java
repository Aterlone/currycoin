package currycoin.script;

public interface Instruction {
    void execute(ScriptStack stack);
}
