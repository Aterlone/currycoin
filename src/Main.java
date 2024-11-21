import javax.crypto.*;
import java.security.*;
import java.util.*; 

public class Main {
	public static String bytesToHex(byte[] arr) {
		String hex = "";
		for (byte i : arr) {
			hex += String.format("%02X", i);
		}
		return hex;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		MessageDigest mess = MessageDigest.getInstance("SHA-256");
		mess.update("Hello".getBytes());

		String md = bytesToHex(mess.digest());
		System.out.println(md);
	}
}