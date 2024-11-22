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
        String stackString = "";
        for (ByteArray byteArray : stack) {
            stackString += byteArray.toString();
            stackString += "\n";
        }
        return stackString;
    }

}