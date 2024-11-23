import currycoin.BlockHeader;
import currycoin.Hash;
import currycoin.script.ByteArray;
import currycoin.script.Script;
import currycoin.script.ScriptStack;
import currycoin.script.instructions.*;

import java.nio.ByteBuffer;
import java.security.*;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import static currycoin.script.instructions.LoadInstruction.loadInt;
import static currycoin.script.instructions.LoadInstruction.ofData;
import static currycoin.script.instructions.OrdinaryInstruction.*;

public class Main {

	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
		MessageDigest mess = MessageDigest.getInstance("SHA-256");
		mess.update("Hi".getBytes());
		Hash prevHash = new Hash(mess.digest());

		mess.update("aefiajfoij".getBytes());
		Hash transHash = new Hash(mess.digest());

		BlockHeader block = new BlockHeader(prevHash, transHash,3);
//		System.out.println(block);
//		System.out.println(block.hash());



		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("EC");
//		keyPairGen.initialize(256);
		KeyPair pair = keyPairGen.generateKeyPair();

		PrivateKey privKey = pair.getPrivate();
		PublicKey publicKey = pair.getPublic();

		ByteArray publicKeyBytes = new ByteArray(publicKey.getEncoded());
		getW

		Hash hash = Hash.empty();

		ScriptStack stack = new ScriptStack(hash);
		System.out.println(stack);

		Signature sig = Signature.getInstance("SHA256withECDSA");
		sig.initSign(privKey);
		sig.update(hash.data());
		ByteArray signatureBytes = new ByteArray(sig.sign());

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
				SIGNATURE_CHECK
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

	}
}