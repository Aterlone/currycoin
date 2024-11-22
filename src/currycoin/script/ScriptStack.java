package currycoin.script;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class ScriptStack {
    private final Deque<ByteArray> stack = new ArrayDeque<>();

    public void push(ByteArray byteArray) {
        stack.push(byteArray);
    }

    public ByteArray pop() {
        return stack.pop();
    }

    public ByteArray peek() {
        return stack.peek();
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

}