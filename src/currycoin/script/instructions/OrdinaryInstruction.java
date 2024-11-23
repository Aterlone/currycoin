package currycoin.script.instructions;

import currycoin.script.ByteArray;
import currycoin.script.ScriptException;
import currycoin.script.ScriptStack;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.lang.Math.*;

public enum OrdinaryInstruction implements Instruction {
	PUSH_1 {
		public void execute(ScriptStack stack) throws ScriptException {
			stack.push(ByteArray.fromInt(-1));
		}
	},
	PUSH_NEGATIVE_1 {
		public void execute(ScriptStack stack) throws ScriptException {
			stack.push(ByteArray.fromInt(-1));
		}
	},
	NO_OPERATION {
		public void execute(ScriptStack stack) throws ScriptException { }
	},
	VERIFY {
		public void execute(ScriptStack stack) throws ScriptException {
			if (!stack.pop().asBoolean()) throw new ScriptException.VerificationException("VERIFY failed");
		}
	},
	RETURN_FAIL {
		public void execute(ScriptStack stack) throws ScriptException {
			throw new ScriptException.VerificationException("Script threw RETURN_FAIL");
		}
	},
	DUPLICATE_CONDITIONAL {
		public void execute(ScriptStack stack) throws ScriptException {
			ByteArray value = stack.peek();
			if (value.asBoolean()) stack.push(value);
		}
	},
	GET_STACK_DEPTH {
		public void execute(ScriptStack stack) throws ScriptException {
			stack.push(ByteArray.fromInt(stack.depth()));
		}
	},
	DROP_ITEM {
		public void execute(ScriptStack stack) throws ScriptException {
			stack.pop();
		}
	},
	DUPLICATE_ITEM {
		public void execute(ScriptStack stack) throws ScriptException {
			stack.push(stack.peek());
		}
	},
	REMOVE_SECOND {
		public void execute(ScriptStack stack) throws ScriptException {
			ByteArray value = stack.pop();
			stack.pop();
			stack.push(value);
		}
	},
	COPY_SECOND {
		public void execute(ScriptStack stack) throws ScriptException {
			ByteArray top = stack.pop();
			ByteArray second = stack.peek();
			stack.push(top);
			stack.push(second);
		}
	},
	COPY_ITEM {
		public void execute(ScriptStack stack) throws ScriptException {
			int index = stack.peek().toInt();
			var removed = Stream.generate(stack::pop).limit(index + 1).toList();
			ByteArray selected = stack.peek(); // keep the old one too
			removed.reversed().forEach(stack::push);
			stack.push(selected);
		}
	},
	ROLL_ITEM {
		public void execute(ScriptStack stack) throws ScriptException {
			int index = stack.peek().toInt();
			var removed = Stream.generate(stack::pop).limit(index + 1).toList();
			ByteArray selected = stack.pop(); // remove it
			removed.reversed().forEach(stack::push);
			stack.push(selected);
		}
	},
	ROTATE_THREE {
		public void execute(ScriptStack stack) throws ScriptException {
			ByteArray first = stack.pop();
			ByteArray second = stack.pop();
			ByteArray third = stack.pop();
			stack.push(second);
			stack.push(first);
			stack.push(third);
		}
	},
	SWAP_TWO {
		public void execute(ScriptStack stack) throws ScriptException {
			ByteArray first = stack.pop();
			ByteArray second = stack.pop();
			stack.push(first);
			stack.push(second);
		}
	},
	TUCK {
		public void execute(ScriptStack stack) throws ScriptException {
			ByteArray top = stack.pop();
			ByteArray second = stack.pop();
			stack.push(top);
			stack.push(second);
			stack.push(top);
		}
	},
	DROP_TWO {
		public void execute(ScriptStack stack) throws ScriptException {
			stack.pop();
			stack.pop();
		}
	},
	DUPLICATE_TWO {
		public void execute(ScriptStack stack) throws ScriptException {
			ByteArray first = stack.pop();
			ByteArray second = stack.pop();
			stack.push(second);
			stack.push(first);
			stack.push(second);
			stack.push(first);
		}
	},
	DUPLICATE_THREE {
		public void execute(ScriptStack stack) throws ScriptException {
			ByteArray first = stack.pop();
			ByteArray second = stack.pop();
			ByteArray third = stack.pop();
			stack.push(third);
			stack.push(second);
			stack.push(first);
			stack.push(third);
			stack.push(second);
			stack.push(first);
		}
	},
	COPY_SECOND_PAIR {
		public void execute(ScriptStack stack) throws ScriptException {
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
		}
	},
	ROTATE_THREE_PAIRS {
		public void execute(ScriptStack stack) throws ScriptException {
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
		}
	},
	SWAP_TWO_PAIRS {
		public void execute(ScriptStack stack) throws ScriptException {
			ByteArray first = stack.pop();
			ByteArray second = stack.pop();
			ByteArray third = stack.pop();
			ByteArray fourth = stack.pop();
			stack.push(second);
			stack.push(first);
			stack.push(fourth);
			stack.push(third);
		}
	},
	GET_ITEM_SIZE {
		public void execute(ScriptStack stack) throws ScriptException {
			stack.push(ByteArray.fromInt(stack.peek().length()));
		}
	},
	BYTES_EQUAL {
		public void execute(ScriptStack stack) throws ScriptException {
			ByteArray first = stack.pop();
			ByteArray second = stack.pop();
			int result = first.equals(second) ? 1 : 0;
			stack.push(ByteArray.fromInt(result));
		}
	},
	VERIFY_BYTES_EQUAL {
		public void execute(ScriptStack stack) throws ScriptException {
			ByteArray first = stack.pop();
			ByteArray second = stack.pop();
			if (!first.equals(second)) throw new ScriptException.VerificationException("VERIFY_BYTES_EQUAL failed");
		}
	},
	ARITHMETIC_ADD1 {
		public void execute(ScriptStack stack) throws ScriptException {
			int val = stack.pop().toInt();
			stack.push(ByteArray.fromInt(++val));
		}
	},
	ARITHMETIC_SUB1 {
		public void execute(ScriptStack stack) throws ScriptException {
			int val = stack.pop().toInt();
			stack.push(ByteArray.fromInt(--val));
		}
	},
	ARITHMETIC_NEG {
		public void execute(ScriptStack stack) throws ScriptException {
			int val = stack.pop().toInt();
			stack.push(ByteArray.fromInt(-1 * val));
		}
	},
	ARITHMETIC_ABS {
		public void execute(ScriptStack stack) throws ScriptException {
			int val = stack.pop().toInt();
			stack.push(ByteArray.fromInt(abs(val)));
		}
	},
	ARITHMETIC_NOT {
		public void execute(ScriptStack stack) throws ScriptException {
			int val = stack.pop().toInt();
			boolean boolVal = (val == 0);
			stack.push(ByteArray.fromInt(boolVal ? 1 : 0));
		}
	},
	ARITHMETIC_0NOTEQUAL {
		public void execute(ScriptStack stack) throws ScriptException {
			int val = stack.pop().toInt();
			boolean boolVal = (val != 0);
			stack.push(ByteArray.fromInt(boolVal ? 1 : 0));
		}
	},
	ARITHMETIC_ADD {
		public void execute(ScriptStack stack) throws ScriptException {
			int first = stack.pop().toInt();
			int second = stack.pop().toInt();
			stack.push(ByteArray.fromInt(second + first));
		}
	},
	ARITHMETIC_SUB {
		public void execute(ScriptStack stack) throws ScriptException {
			int first = stack.pop().toInt();
			int second = stack.pop().toInt();
			stack.push(ByteArray.fromInt(second - first));
		}
	},
	ARITHMETIC_BOOLAND {
		public void execute(ScriptStack stack) throws ScriptException {
			int first = stack.pop().toInt();
			int second = stack.pop().toInt();
			stack.push(ByteArray.fromInt((first != 0 && second != 0) ? 1 : 0));
		}
	},
	ARITHMETIC_BOOLOR {
		public void execute(ScriptStack stack) throws ScriptException {
			int first = stack.pop().toInt();
			int second = stack.pop().toInt();
			stack.push(ByteArray.fromInt((first != 0 || second != 0) ? 1 : 0));
		}
	},
	ARITHMETIC_NUMEQUAL {
		public void execute(ScriptStack stack) throws ScriptException {
			int first = stack.pop().toInt();
			int second = stack.pop().toInt();
			stack.push(ByteArray.fromInt((first == second) ? 1 : 0));
		}
	},
	ARITHMETIC_NUMEQUALVERIFY {
		public void execute(ScriptStack stack) throws ScriptException {
			int first = stack.pop().toInt();
			int second = stack.pop().toInt();
			stack.push(ByteArray.fromInt((first == second) ? 1 : 0));
			if (first != second) throw new ScriptException.VerificationException("ARITHMETIC_NUMEQUALVERIFY failed");
		}
	},
	ARITHMETIC_NUMNOTEQUAL {
		public void execute(ScriptStack stack) throws ScriptException {
			int first = stack.pop().toInt();
			int second = stack.pop().toInt();
			stack.push(ByteArray.fromInt((first != second) ? 1 : 0));
		}
	},
	ARITHMETIC_LESSTHAN {
		public void execute(ScriptStack stack) throws ScriptException {
			int first = stack.pop().toInt();
			int second = stack.pop().toInt();
			stack.push(ByteArray.fromInt((second < first) ? 1 : 0));
		}
	},
	ARITHMETIC_GREATERTHAN {
		public void execute(ScriptStack stack) throws ScriptException {
			int first = stack.pop().toInt();
			int second = stack.pop().toInt();
			stack.push(ByteArray.fromInt((second > first) ? 1 : 0));
		}
	},
	ARITHMETIC_LESSTHANOREQUAL {
		public void execute(ScriptStack stack) throws ScriptException {
			int first = stack.pop().toInt();
			int second = stack.pop().toInt();
			stack.push(ByteArray.fromInt((second <= first) ? 1 : 0));
		}
	},
	ARITHMETIC_GREATERTHANOREQUAL {
		public void execute(ScriptStack stack) throws ScriptException {
			int first = stack.pop().toInt();
			int second = stack.pop().toInt();
			stack.push(ByteArray.fromInt((second >= first) ? 1 : 0));
		}
	},
	ARITHMETIC_MIN {
		public void execute(ScriptStack stack) throws ScriptException {
			int first = stack.pop().toInt();
			int second = stack.pop().toInt();
			stack.push(ByteArray.fromInt(min(second, first)));
		}
	},
	ARITHMETIC_MAX {
		public void execute(ScriptStack stack) throws ScriptException {
			int first = stack.pop().toInt();
			int second = stack.pop().toInt();
			stack.push(ByteArray.fromInt(max(second, first)));
		}
	},
	ARITHMETIC_WITHIN {
		public void execute(ScriptStack stack) throws ScriptException {
			int max = stack.pop().toInt();
			int min = stack.pop().toInt();
			int value = stack.pop().toInt();
			stack.push(ByteArray.fromInt((value >= min && value <= max) ? 1 : 0));
		}
	},
	HASH_SHA256 {
		public void execute(ScriptStack stack) throws ScriptException {
			ByteArray value = stack.pop();
			ByteArray byteArray;
			try {
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				value.addToDigest(digest);
				byteArray = new ByteArray(digest.digest());
			} catch (NoSuchAlgorithmException e) {
				throw new ScriptException.InvalidScriptException("SHA-256 not supported", e);
			}
			stack.push(byteArray);
		}
	},
	HASH_TWICE_SHA256 {
		public void execute(ScriptStack stack) throws ScriptException {
			ByteArray value = stack.pop();
			ByteArray byteArray;
			try {
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				value.addToDigest(digest);
				value.addToDigest(digest);
				byteArray = new ByteArray(digest.digest());
			} catch (NoSuchAlgorithmException e) {
				throw new ScriptException.InvalidScriptException("SHA-256 not supported", e);
			}
			stack.push(byteArray);
		}
	};

	public static final byte FIRST_OPCODE = ConditionalBlock.ENDIF_OPCODE + 1;

	@Override
	public abstract void execute(ScriptStack stack) throws ScriptException;

	@Override
	public int byteSize() {
		return 1;
	}

	@Override
	public void apply(ByteBuffer buffer) {
		buffer.put((byte) (FIRST_OPCODE + ordinal()));
	}
}