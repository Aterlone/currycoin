package currycoin.script.instructions;

import currycoin.script.ByteArray;
import currycoin.script.ScriptStack;

import java.util.function.Function;
import java.util.stream.Stream;

import static java.lang.Math.*;

public enum OrdinaryInstruction implements Instruction {
	PUSH_1(stack -> {
		stack.push(ByteArray.fromInt(-1));
		return true;
	}),
	PUSH_NEGATIVE_1(stack -> {
		stack.push(ByteArray.fromInt(-1));
		return true;
	}),
	NO_OPERATION(stack -> true),
	VERIFY(stack -> stack.pop().asBoolean()),
	RETURN_FAIL(stack -> false),
	DUPLICATE_CONDITIONAL(stack -> {
		ByteArray value = stack.peek();
		if (value.asBoolean()) stack.push(value);
		return true;
	}),
	GET_STACK_DEPTH(stack -> {
		stack.push(ByteArray.fromInt(stack.depth()));
		return true;
	}),
	DROP_ITEM(stack -> {
		stack.pop();
		return true;
	}),
	DUPLICATE_ITEM(stack -> {
		stack.push(stack.peek());
		return true;
	}),
	REMOVE_SECOND(stack -> {
		ByteArray value = stack.pop();
		stack.pop();
		stack.push(value);
		return true;
	}),
	COPY_SECOND(stack -> {
		ByteArray top = stack.pop();
		ByteArray second = stack.peek();
		stack.push(top);
		stack.push(second);
		return true;
	}),
	COPY_ITEM(stack -> {
		int index = stack.peek().toInt();
		var removed = Stream.generate(stack::pop).limit(index + 1).toList();
		ByteArray selected = stack.peek(); // keep the old one too
		removed.reversed().forEach(stack::push);
		stack.push(selected);
		return true;
	}),
	ROLL_ITEM(stack -> {
		int index = stack.peek().toInt();
		var removed = Stream.generate(stack::pop).limit(index + 1).toList();
		ByteArray selected = stack.pop(); // remove it
		removed.reversed().forEach(stack::push);
		stack.push(selected);
		return true;
	}),
	ROTATE_THREE(stack -> {
		ByteArray first = stack.pop();
		ByteArray second = stack.pop();
		ByteArray third = stack.pop();
		stack.push(second);
		stack.push(first);
		stack.push(third);
		return true;
	}),
	SWAP_TWO(stack -> {
		ByteArray first = stack.pop();
		ByteArray second = stack.pop();
		stack.push(first);
		stack.push(second);
		return true;
	}),
	TUCK(stack -> {
		ByteArray top = stack.pop();
		ByteArray second = stack.pop();
		stack.push(top);
		stack.push(second);
		stack.push(top);
		return true;
	}),
	DROP_TWO(stack -> {
		stack.pop();
		stack.pop();
		return true;
	}),
	DUPLICATE_TWO(stack -> {
		ByteArray first = stack.pop();
		ByteArray second = stack.pop();
		stack.push(second);
		stack.push(first);
		stack.push(second);
		stack.push(first);
		return true;
	}),
	DUPLICATE_THREE(stack -> {
		ByteArray first = stack.pop();
		ByteArray second = stack.pop();
		ByteArray third = stack.pop();
		stack.push(third);
		stack.push(second);
		stack.push(first);
		stack.push(third);
		stack.push(second);
		stack.push(first);
		return true;
	}),
	COPY_SECOND_PAIR(stack -> {
		ByteArray first = stack.pop();
		ByteArray second = stack.pop();
		ByteArray third = stack.pop();
		ByteArray fourth = stack.pop();
		stack.push(fourth);
		stack.push(third);
		stack.push(second);
		stack.push(first);
		stack.push(fourth);
		stack.push(third);
		return true;
	}),
	ROTATE_THREE_PAIRS(stack -> {
		ByteArray first = stack.pop();
		ByteArray second = stack.pop();
		ByteArray third = stack.pop();
		ByteArray fourth = stack.pop();
		ByteArray fifth = stack.pop();
		ByteArray sixth = stack.pop();
		stack.push(fourth);
		stack.push(third);
		stack.push(second);
		stack.push(first);
		stack.push(sixth);
		stack.push(fifth);
		return true;
	}),
	SWAP_TWO_PAIRS(stack -> {
		ByteArray first = stack.pop();
		ByteArray second = stack.pop();
		ByteArray third = stack.pop();
		ByteArray fourth = stack.pop();
		stack.push(second);
		stack.push(first);
		stack.push(fourth);
		stack.push(third);
		return true;
	}),
	GET_ITEM_SIZE(stack -> {
		stack.push(ByteArray.fromInt(stack.peek().length()));
		return true;
	}),
	BYTES_EQUAL(stack -> {
		ByteArray first = stack.pop();
		ByteArray second = stack.pop();
		int result = first.equals(second) ? 1 : 0;
		stack.push(ByteArray.fromInt(result));
		return true;
	}),
	VERIFY_BYTES_EQUAL(stack -> {
		ByteArray first = stack.pop();
		ByteArray second = stack.pop();
		return first.equals(second);
	}),
	ARITHMETIC_ADD1(stack -> {
		int val = stack.pop().toInt();
		stack.push(ByteArray.fromInt(++val));
		return true;
	}),
	ARITHMETIC_SUB1(stack -> {
		int val = stack.pop().toInt();
		stack.push(ByteArray.fromInt(--val));
		return true;
	}),
	ARITHMETIC_NEG(stack -> {
		int val = stack.pop().toInt();
		stack.push(ByteArray.fromInt(-1 * val));
		return true;
	}),
	ARITHMETIC_ABS(stack -> {
		int val = stack.pop().toInt();
		stack.push(ByteArray.fromInt(abs(val)));
		return true;
	}),
	ARITHMETIC_NOT(stack -> {
		int val = stack.pop().toInt();
		boolean boolVal = (val == 0);
		stack.push(ByteArray.fromInt(boolVal ? 1 : 0));
		return true;
	}),
	ARITHMETIC_0NOTEQUAL(stack -> {
		int val = stack.pop().toInt();
		boolean boolVal = (val != 0);
		stack.push(ByteArray.fromInt(boolVal ? 1 : 0));
		return true;
	}),
	ARITHMETIC_ADD(stack -> {
		int first = stack.pop().toInt();
		int second = stack.pop().toInt();
		stack.push(ByteArray.fromInt(second + first));
		return true;
	}),
	ARITHMETIC_SUB(stack -> {
		int first = stack.pop().toInt();
		int second = stack.pop().toInt();
		stack.push(ByteArray.fromInt(second - first));
		return true;
	}),
	ARITHMETIC_BOOLAND(stack -> {
		int first = stack.pop().toInt();
		int second = stack.pop().toInt();
		stack.push(ByteArray.fromInt((first != 0 && second != 0) ? 1 : 0));
		return true;
	}),
	ARITHMETIC_BOOLOR(stack -> {
		int first = stack.pop().toInt();
		int second = stack.pop().toInt();
		stack.push(ByteArray.fromInt((first != 0 || second != 0) ? 1 : 0));
		return true;
	}),
	ARITHMETIC_NUMEQUAL(stack -> {
		int first = stack.pop().toInt();
		int second = stack.pop().toInt();
		stack.push(ByteArray.fromInt((first == second) ? 1 : 0));
		return true;
	}),
	ARITHMETIC_NUMEQUALVERIFY(stack -> {
		int first = stack.pop().toInt();
		int second = stack.pop().toInt();
		stack.push(ByteArray.fromInt((first == second) ? 1 : 0));
		return first == second;
	}),
	ARITHMETIC_NUMNOTEQUAL(stack -> {
		int first = stack.pop().toInt();
		int second = stack.pop().toInt();
		stack.push(ByteArray.fromInt((first != second) ? 1 : 0));
		return true;
	}),
	ARITHMETIC_LESSTHAN(stack -> {
		int first = stack.pop().toInt();
		int second = stack.pop().toInt();
		stack.push(ByteArray.fromInt((first < second) ? 1 : 0));
		return true;
	}),
	ARITHMETIC_GREATERTHAN(stack -> {
		int first = stack.pop().toInt();
		int second = stack.pop().toInt();
		stack.push(ByteArray.fromInt((first > second) ? 1 : 0));
		return true;
	}),
	ARITHMETIC_LESSTHANOREQUAL(stack -> {
		int first = stack.pop().toInt();
		int second = stack.pop().toInt();
		stack.push(ByteArray.fromInt((first <= second) ? 1 : 0));
		return true;
	}),
	ARITHMETIC_GREATERTHANOREQUAL(stack -> {
		int first = stack.pop().toInt();
		int second = stack.pop().toInt();
		stack.push(ByteArray.fromInt((first >= second) ? 1 : 0));
		return true;
	}),
	ARITHMETIC_MIN(stack -> {
		int first = stack.pop().toInt();
		int second = stack.pop().toInt();
		stack.push(ByteArray.fromInt(min(first, second)));
		return true;
	}),
	ARITHMETIC_MAX(stack -> {
		int first = stack.pop().toInt();
		int second = stack.pop().toInt();
		stack.push(ByteArray.fromInt(max(first, second)));
		return true;
	}),
	ARITHMETIC_WITHIN(stack -> {
		int first = stack.pop().toInt();
		int min = stack.pop().toInt();
		int max = stack.pop().toInt();
		stack.push(ByteArray.fromInt((first >= min && first < max) ? 1 : 0));
		return true;
	});

	private final Function<ScriptStack, Boolean> function;
	OrdinaryInstruction(Function<ScriptStack, Boolean> function) {
		this.function = function;
	}

	@Override
	public boolean execute(ScriptStack stack) {
		return function.apply(stack);
	}
}