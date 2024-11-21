import currycoin.BlockHeader;
import currycoin.Hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
	}
}