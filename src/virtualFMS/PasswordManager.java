package virtualFMS;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordManager {

	// Generates a hash for the password
	public static String hash(String pwd, byte[] nacl) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(nacl);
			byte[] bytes = md.digest(pwd.getBytes());
			BigInteger no = new BigInteger(1, bytes);;
			return no.toString(16);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	public static String hash(String pwd, String nacl) {
		byte[] naclA = nacl.getBytes();
		return hash(pwd, naclA);
	}
	
	// Checks the password criteria
	public static boolean checkPassword(String pwd) {
		char[] charPwd = pwd.toCharArray();
		if (charPwd.length >= 15) {
			Pattern lowerLetter = Pattern.compile("[a-z]");
			Pattern upperLetter = Pattern.compile("[A-z]");
			Pattern digit = Pattern.compile("[0-9]");
			Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

			Matcher hasLowLetter = lowerLetter.matcher(pwd);
			Matcher hasUpLetter = upperLetter.matcher(pwd);
			Matcher hasDigit = digit.matcher(pwd);
			Matcher hasSpecial = special.matcher(pwd);

			return hasLowLetter.find() && hasDigit.find() && hasSpecial.find() && hasUpLetter.find();
		}
		return false;
	}

	public static byte[] salt() {
		SecureRandom random = new SecureRandom();
		byte[] nacl = new byte[16];
		random.nextBytes(nacl);
		return nacl;
	}
}
//ThisIsMyPassword1!