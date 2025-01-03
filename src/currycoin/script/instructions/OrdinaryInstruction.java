package currycoin.script.instructions;

import currycoin.Hash;
import currycoin.script.ByteArray;
import currycoin.script.ScriptException;
import currycoin.script.ScriptStack;

import java.nio.ByteBuffer;
import java.security.*;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;
import java.util.ArrayDeque;
import java.util.Deque;
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
			int index = stack.pop().toInt(); // gets removed
			var removed = Stream.generate(stack::pop).limit(index).toList();
			ByteArray selected = stack.peek(); // keep the old one too
			removed.forEach(stack::push);
			stack.push(selected);
		}
	},
	ROLL_ITEM {
		public void execute(ScriptStack stack) throws ScriptException {
			int index = stack.pop().toInt(); // gets removed
			var removed = Stream.generate(stack::pop).limit(index).toList();
			ByteArray selected = stack.pop(); // remove it
			removed.forEach(stack::push);
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
	ARITHMETIC_ADD_1 {
		public void execute(ScriptStack stack) throws ScriptException {
			int val = stack.pop().toInt();
			stack.push(ByteArray.fromInt(val + 1));
		}
	},
	ARITHMETIC_SUB_1 {
		public void execute(ScriptStack stack) throws ScriptException {
			int val = stack.pop().toInt();
			stack.push(ByteArray.fromInt(val - 1));
		}
	},
	ARITHMETIC_NEG {
		public void execute(ScriptStack stack) throws ScriptException {
			int val = stack.pop().toInt();
			stack.push(ByteArray.fromInt(-val));
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
	ARITHMETIC_0_NOT_EQUAL {
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
	ARITHMETIC_BOOL_AND {
		public void execute(ScriptStack stack) throws ScriptException {
			int first = stack.pop().toInt();
			int second = stack.pop().toInt();
			stack.push(ByteArray.fromInt((first != 0 && second != 0) ? 1 : 0));
		}
	},
	ARITHMETIC_BOOL_OR {
		public void execute(ScriptStack stack) throws ScriptException {
			int first = stack.pop().toInt();
			int second = stack.pop().toInt();
			stack.push(ByteArray.fromInt((first != 0 || second != 0) ? 1 : 0));
		}
	},
	ARITHMETIC_NUM_EQUAL {
		public void execute(ScriptStack stack) throws ScriptException {
			int first = stack.pop().toInt();
			int second = stack.pop().toInt();
			stack.push(ByteArray.fromInt((first == second) ? 1 : 0));
		}
	},
	ARITHMETIC_NUM_EQUAL_VERIFY {
		public void execute(ScriptStack stack) throws ScriptException {
			int first = stack.pop().toInt();
			int second = stack.pop().toInt();
			stack.push(ByteArray.fromInt((first == second) ? 1 : 0));
			if (first != second) throw new ScriptException.VerificationException("ARITHMETIC_NUMEQUALVERIFY failed");
		}
	},
	ARITHMETIC_NUM_NOT_EQUAL {
		public void execute(ScriptStack stack) throws ScriptException {
			int first = stack.pop().toInt();
			int second = stack.pop().toInt();
			stack.push(ByteArray.fromInt((first != second) ? 1 : 0));
		}
	},
	ARITHMETIC_LESS_THAN {
		public void execute(ScriptStack stack) throws ScriptException {
			int first = stack.pop().toInt();
			int second = stack.pop().toInt();
			stack.push(ByteArray.fromInt((second < first) ? 1 : 0));
		}
	},
	ARITHMETIC_GREATER_THAN {
		public void execute(ScriptStack stack) throws ScriptException {
			int first = stack.pop().toInt();
			int second = stack.pop().toInt();
			stack.push(ByteArray.fromInt((second > first) ? 1 : 0));
		}
	},
	ARITHMETIC_LESS_THAN_OR_EQUAL {
		public void execute(ScriptStack stack) throws ScriptException {
			int first = stack.pop().toInt();
			int second = stack.pop().toInt();
			stack.push(ByteArray.fromInt((second <= first) ? 1 : 0));
		}
	},
	ARITHMETIC_GREATER_THAN_OR_EQUAL {
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
			try {
				ByteArray value = stack.pop();

				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				value.addToDigest(digest);

				stack.push(new ByteArray(digest.digest()));
			} catch (NoSuchAlgorithmException e) {
				throw new ScriptException.InvalidScriptException("SHA-256 not supported", e);
			}
		}
	},
	HASH_TWICE_SHA256 {
		public void execute(ScriptStack stack) throws ScriptException {
			try {
				ByteArray value = stack.pop();

				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				value.addToDigest(digest);
				MessageDigest digest2 = MessageDigest.getInstance("SHA-256");
				digest2.update(digest.digest());

				stack.push(new ByteArray(digest2.digest()));
			} catch (NoSuchAlgorithmException e) {
				throw new ScriptException.InvalidScriptException("SHA-256 not supported", e);
			}
		}
	},
	SIGNATURE_CHECK {
		public void execute(ScriptStack stack) throws ScriptException {
			ByteArray publicKey = stack.pop();
			ByteArray signature = stack.pop();
			Hash hash = stack.dataToSign();

			boolean verified = checkSignature(signature, publicKey, hash);
			stack.push(ByteArray.fromInt(verified ? 1 : 0));
        }
	},
	SIGNATURE_CHECK_VERIFY {
		public void execute(ScriptStack stack) throws ScriptException {
			ByteArray publicKey = stack.pop();
			ByteArray signature = stack.pop();
			Hash hash = stack.dataToSign();

			boolean verified = checkSignature(signature, publicKey, hash);
			if (!verified)
				throw new ScriptException.VerificationException("Signature does not match!!!!!");
		}
	},
	SIGNATURE_CHECK_ADD {
		public void execute(ScriptStack stack) throws ScriptException {
			int n = stack.pop().toInt();

			ByteArray publicKey = stack.pop();
			ByteArray signature = stack.pop();
			Hash hash = stack.dataToSign();

			boolean verified = checkSignature(signature, publicKey, hash);
			stack.push(ByteArray.fromInt(verified ? n + 1 : n));
		}
	},
	SIGNATURE_CHECK_MULTI {
		public void execute(ScriptStack stack) throws ScriptException {
			Hash hash = stack.dataToSign();

			int n = stack.pop().toInt();
			Deque<ByteArray> publicKeys = new ArrayDeque<>(n);
			for (int i = 0; i < n; i++) {
				publicKeys.push(stack.pop());
			}

			int m = stack.pop().toInt();
			boolean verified = true;
			for (int i = 0; i < m; i++) {
				ByteArray signature = stack.pop();

				if (!hasSignatureMatch(signature, publicKeys, hash)) {
					verified = false;
					break;
				}
			}

			stack.push(ByteArray.fromInt(verified ? 1 : 0));
		}
	},
	SIGNATURE_CHECK_MULTI_VERIFY {
		public void execute(ScriptStack stack) throws ScriptException {
			Hash hash = stack.dataToSign();

			int n = stack.pop().toInt();
			Deque<ByteArray> publicKeys = new ArrayDeque<>(n);
			for (int i = 0; i < n; i++) {
				publicKeys.offerLast(stack.pop());
			}

			int m = stack.pop().toInt();
			for (int i = 0; i < m; i++) {
				ByteArray signature = stack.pop();

				if (!hasSignatureMatch(signature, publicKeys, hash)) {
					throw new ScriptException.VerificationException("Multi-signature verification failed");
				}
			}
		}
	},
	;

	private static boolean checkSignature(ByteArray signature, ByteArray publicKey, Hash hash) throws ScriptException {
		try {
			KeyFactory keyFactoryEC = KeyFactory.getInstance("EC");
			ECPublicKey ecPublicKey = (ECPublicKey) keyFactoryEC.generatePublic(new X509EncodedKeySpec(publicKey.data()));

			Signature sig = Signature.getInstance("SHA256withECDSA");
			sig.initVerify(ecPublicKey);
			sig.update(hash.data());

			return sig.verify(signature.data());
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw new ScriptException.InvalidScriptException("EC not supported", e);
		} catch (InvalidKeySpecException | SignatureException e) {
			return false; // this is a mal-formatted signature, not a system failure
		}
	}

	private static boolean hasSignatureMatch(ByteArray signature, Deque<ByteArray> publicKeys, Hash hash) throws ScriptException {
		while (!publicKeys.isEmpty()) {
			ByteArray publicKey = publicKeys.pop();
			if (checkSignature(signature, publicKey, hash)) {
				return true;
			}
		}
		return false;
	}

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