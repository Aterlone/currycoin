package currycoin.script;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ScriptStack {

    private final ArrayList<ByteBuffer> stack = new ArrayList<ByteBuffer>();

    private void outOfBounds(int index) {
        if (stack.isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("No array value.");
        } else {
            if (stack.size() - (index) < 0) {
                throw new ArrayIndexOutOfBoundsException("Accessed element out of bounds.");
            }
        }
    }

    private void pushIndex(int index) {
        outOfBounds(index);
        stack.add(stack.get(index));
    }

    private void popIndex(int index) {
        outOfBounds(index);
        stack.remove(index);
    }

    /// Pushes values to top of the stack. This method can be used for other push types.
    public void push(ByteBuffer data) {
        stack.add(data);
    }


    /* Stack Specific Implementation */

    // Total Stack From Alt Stack to implement.

    /// Duplicates top item on stack if it is not 0.
    public void dupIfNZero() {

    }

    // Depth to implement

    /// Drops the top of the stack.
    public void pop() {
        popIndex(stack.size() - 1);
    }

    /// Duplicated the top item on the stack.
    public void dup() {
        pushIndex(stack.size() - 1);
    }

    /// Pop the second top item on the stack.
    public void popSec() {
        popIndex(stack.size() - 2);
    }

    /// Copy the second top item to the top of the stack.
    public void dupSec() {
        pushIndex(stack.size() - 2);
    }

    /// Pops the stacksize - index item.
    public void popX(int index) {
        popIndex(stack.size() - (index + 1));
    }

    /// Moves stacksize - index to top of stack.
    public void movX(int index) {
        outOfBounds(stack.size() - (index + 1));
        stack.add(stack.get(stack.size() - (index + 1)));
        stack.remove(stack.size() - (index + 1));
    }

    // 3rd item to top

    /// Swaps the two top stack items.
    public void swap() {
        outOfBounds(stack.size() - 2);
        stack.add(stack.get(stack.size() - 2));
        stack.remove(stack.get(stack.size() - 3));
    }

    /// Top item copied and placed under second to top item. Check if this is correct.
    public void tuck() {
        outOfBounds(stack.size() - 2);
        stack.add(stack.size() - 2, stack.getLast());
    }

    // Remove top two, Duplicate top two, Dup top three, copy two spaces back to front
    //5/6 back to top, swap top two pairs
}
