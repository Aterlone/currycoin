package currycoin.script;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class ScriptStack {

    private final Deque<ByteArray> stack = new ArrayDeque<>();

    private void outOfBounds (int index) {
        if (stack.isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("No array value.");
        } else {
            if (stack.size() - (index) < 0) {
                throw new ArrayIndexOutOfBoundsException("Accessed element out of bounds.");
            }
        }
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