package currycoin.script;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ScriptStack {
    private final ArrayList<ByteBuffer> stack = new ArrayList<ByteBuffer>();

    /// Pushes values to top of the stack.
    public void push(ByteBuffer data) {

    }

    /* Stack Specific Implementation */

    // Total Stack From Alt Stack to implement.

    /// Duplicates top item on stack if it is not 0.
    public void dupIfNZero() {

    }

    // Depth to implement

    /// Drops the top of the stack.
    public void pop() {

    }

    /// Duplicated the top item on the stack.
    public void dup() {

    }

    /// Pop the second top item on the stack.
    public void pop_sec() {

    }

    /// Copy the second top item to the top of the stack.
    public void dup_sec() {

    }

    /// Pops the stacksize - index item.
    public void pop_x(int index) {

    }

    /// Moves stacksize - index to top of stack.
    public void mov_x(int index) {

    }

    // 3rd item to top

    /// Swaps the two top stack items
    public void swap() {

    }

    /// Top item copied under second to top item
    public void tuck() {

    }

    // Remove top two, Duplicate top two, Dup top three, copy two spaces back to front
    //5/6 back to top, swap top two pairs

    public void add(ByteBuffer data, int index) {

    }
}
