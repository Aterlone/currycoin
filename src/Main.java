import block.Block;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {

	public static String bytesToHex(byte[] arr) {
		String hex = "";
		for (byte i : arr) {
			hex += String.format("%02X", i);
		}
		return hex;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		Block block = new Block("Hello".getBytes(),"Hello".getBytes(),3);
		System.out.println(bytesToHex(block.hash()));
		System.out.println(block);
	}
}