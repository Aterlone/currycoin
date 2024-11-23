package currycoin.script;

import currycoin.Hash;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

public class ScriptStack {
    private final Deque<ByteArray> stack = new ArrayDeque<>();
    private final Hash dataToSign;

    public ScriptStack(Hash dataToSign) {
        this.dataToSign = dataToSign;
    }

    public void push(ByteArray byteArray) throws ScriptException {
        stack.push(byteArray);
    }

    public ByteArray pop() throws ScriptException.StackUnderflowException {
        try {
            return stack.pop();
        } catch (NoSuchElementException e) {
            throw new ScriptException.StackUnderflowException("Nothing to pop", e);
        }
    }

    public ByteArray peek() throws ScriptException.StackUnderflowException {
        ByteArray result = stack.peek();
        if (result == null)
            throw new ScriptException.StackUnderflowException("Nothing to peek");
        return result;
    }

    public int depth() {
        return stack.size();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("Size: " + stack.size());

        for (ByteArray byteArray : stack.reversed()) {
            builder.append("\n");
            builder.append(byteArray.toPrettyString());
        }
        return builder.toString();
    }

    public Hash dataToSign() {
        return dataToSign;
    }
}