package currycoin.script;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ScriptStack {

    private final ArrayList<ByteBuffer> stack = new ArrayList<ByteBuffer>();

    private void outOfBounds (int index) {
        if (stack.isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("No array value.");
        } else {
            if (stack.size() - (index) < 0) {
                throw new ArrayIndexOutOfBoundsException("Accessed element out of bounds.");
            }
        }
    }

    public void push (ByteBuffer data) {
        stack.add(data);
    }

    public ByteBuffer pop () {
        outOfBounds(stack.size() - 1);
        return stack.removeLast();
    }

    public ByteBuffer get (int index) {
        outOfBounds(stack.size() - (index + 1));
        return stack.get(stack.size() - (index + 1));
    }

    public void insert (int index, ByteBuffer data) {
        outOfBounds(stack.size() - (index + 1));
        stack.add(stack.size() - (index + 1), data);
    }

    public ByteBuffer remove (int index) {
        outOfBounds(stack.size() - (index + 1));
        ByteBuffer val = stack.get(stack.size() - (index + 1));
        stack.remove(stack.size() - (index + 1));
        return val;
    }

    public String toString() {
        String stackString = "";
        for (ByteBuffer byteBuffer : stack) {
            stackString += new String(byteBuffer.array());
            stackString += "\n";
        }
        return stackString;
    }

}