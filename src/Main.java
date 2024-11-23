import currycoin.BlockHeader;
import currycoin.Hash;
import currycoin.script.ByteArray;
import currycoin.script.Script;
import currycoin.script.ScriptStack;
import currycoin.script.instructions.*;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static currycoin.script.instructions.LoadInstruction.loadInt;
import static currycoin.script.instructions.OrdinaryInstruction.*;

public class Main {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		MessageDigest mess = MessageDigest.getInstance("SHA-256");
		mess.update("Hi".getBytes());
		Hash prevHash = new Hash(mess.digest());

		mess.update("aefiajfoij".getBytes());
		Hash transHash = new Hash(mess.digest());

		BlockHeader block = new BlockHeader(prevHash, transHash,3);
		System.out.println(block);
		System.out.println(block.hash());

		// testing script
		ScriptStack stack = new ScriptStack();
		System.out.println(stack);

		Script script = Script.of(
				loadInt(0xABCD),
				loadInt(1),
				loadInt(2),
				DUPLICATE_TWO,
				ARITHMETIC_ADD,
				ROTATE_THREE,
				ROTATE_THREE,
				ARITHMETIC_SUB
		);

		ByteBuffer buffer = ByteBuffer.allocate(script.byteSize());
		script.apply(buffer);
		buffer.flip();
		Script decoded = Script.parseFrom(buffer);
		buffer.flip();

		System.out.println(script);
		System.out.println(decoded);

		for (Instruction instruction : script.instructions()) {
			System.out.println("\nexecuting " + instruction);
			boolean success = instruction.execute(stack);
			System.out.println("success: " + success);
			System.out.println(stack);
		}

	}
}