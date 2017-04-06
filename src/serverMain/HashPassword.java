package serverMain;

import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Arrays;

public class HashPassword {
	private byte[] salt;
	private String hash;

	public HashPassword(String password) throws NoSuchAlgorithmException, NoSuchProviderException {
		salt = getSaltRandom();
		System.out.println(Arrays.toString(salt) + " i hashpassword"); //remove
		this.hash = getSecurePassword(password, salt);
		System.out.println(hash + " i hashpassword");  //remove
	}
	
	public HashPassword(String password, byte[] salt) {
		this.hash = getSecurePassword(password, salt);
		this.salt=salt;
	}

	private static String getSecurePassword(String passwordToHash, byte[] salt) {
		String generatedPassword = null;
		try {
			// Create MessageDigest instance for MD5
			MessageDigest md = MessageDigest.getInstance("MD5");
			// Add password bytes to digest
			md.update(salt);
			// Get the hash's bytes
			byte[] bytes = md.digest(passwordToHash.getBytes());
			// This bytes[] has bytes in decimal format;
			// Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			// Get complete hashed password in hex format
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}
	protected String comparePasswords(String passwordtoCompare, byte[] bytes){
		return getSecurePassword(passwordtoCompare, bytes);
	}

	// Add salt
	private static byte[] getSaltRandom() throws NoSuchAlgorithmException, NoSuchProviderException {
		// Always use a SecureRandom generator
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
		// Create array for salt
		byte[] salt = new byte[16];
		// Get a random salt
		sr.nextBytes(salt);
		// return salt
		return salt;
	}
	public String getHash() {
		return hash;
	}
	public byte[] getSalt() {
		return salt;
	}
}
