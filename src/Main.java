import currycoin.BlockHeader;
import currycoin.Hash;
import currycoin.script.ByteArray;
import currycoin.script.Script;
import currycoin.script.ScriptException;
import currycoin.script.ScriptStack;
import currycoin.script.instructions.*;

import java.nio.ByteBuffer;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

import static currycoin.script.instructions.ConditionalBlock.whenFalse;
import static currycoin.script.instructions.ConditionalBlock.whenTrue;
import static currycoin.script.instructions.LoadInstruction.loadInt;
import static currycoin.script.instructions.LoadInstruction.ofData;
import static currycoin.script.instructions.OrdinaryInstruction.*;

public class Main {
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("EC");
		keyPairGen.initialize(256);

		KeyPair pair = keyPairGen.generateKeyPair();
		ECPrivateKey privateKey = (ECPrivateKey) pair.getPrivate();
		ECPublicKey publicKey = (ECPublicKey) pair.getPublic();

		System.out.println(keyPairGen.getAlgorithm());
		System.out.println(publicKey.getParams());

		ByteArray publicKeyBytes = new ByteArray(publicKey.getEncoded());

		Hash hash = Hash.empty();

		ScriptStack stack = new ScriptStack(hash);
		System.out.println(stack);

		Signature sig = Signature.getInstance("SHA256withECDSA");
		sig.initSign(privateKey);
		sig.update(hash.data());
		ByteArray signatureBytes = new ByteArray(sig.sign());

		System.out.println("sig: " + signatureBytes.length() + " bytes");
		System.out.println("pub: " + publicKeyBytes.length() + " bytes");

		Script script = Script.of(
				loadInt(0xABCD),
				loadInt(1),
				loadInt(2),
				DUPLICATE_TWO,
				ARITHMETIC_ADD,
				ROTATE_THREE,
				ROTATE_THREE,
				ARITHMETIC_SUB,
				ofData(signatureBytes),
				ofData(publicKeyBytes),
				SIGNATURE_CHECK,
				whenFalse(RETURN_FAIL)
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
			instruction.execute(stack);
			System.out.println(stack);
		}

		System.out.println("\n\nsuccessful; testing tampered signature now\n");

		// try tampering with the signature
		try {
			byte[] tampered = signatureBytes.data();
			tampered[0]++;
			ByteArray tamperedSignatureBytes = new ByteArray(tampered);

			Script tamperedScript = Script.of(
					ofData(tamperedSignatureBytes),
					ofData(publicKeyBytes),
					SIGNATURE_CHECK_VERIFY
			);

			stack = new ScriptStack(hash);

			for (Instruction instruction : tamperedScript.instructions()) {
				System.out.println("\nexecuting " + instruction);
				instruction.execute(stack);
				System.out.println(stack);
			}
		} catch (ScriptException.VerificationException e) {
			System.out.println("Verification Exception: " + e.getMessage());
		}

		System.out.println("\n\nsuccessful; testing tampered pubkey now\n");

		// try tampering with the pubkey
		try {
			byte[] tampered = publicKeyBytes.data();
			tampered[0]++;
			ByteArray tamperedPublicKeyBytes = new ByteArray(tampered);

			Script tamperedScript = Script.of(
					ofData(signatureBytes),
					ofData(tamperedPublicKeyBytes),
					SIGNATURE_CHECK,
					ARITHMETIC_NOT,
					VERIFY
			);

			stack = new ScriptStack(hash);

			for (Instruction instruction : tamperedScript.instructions()) {
				System.out.println("\nexecuting " + instruction);
				instruction.execute(stack);
				System.out.println(stack);
			}
		} catch (ScriptException.VerificationException e) {
			System.out.println("Verification Exception: " + e.getMessage());
		}
	}
}