package virtualFMS;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*

	For hashing security.
	https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/

*/
public class PasswordManager {

	// Generates a hash for the password
	public static String hash(String pwd) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-512");
			md.update(pwd.getBytes());
			byte[] bytes = md.digest();
			StringBuilder hashBuilder = new StringBuilder();
			for (int i = 0; i < bytes.length; i++)
				hashBuilder.append(Integer.toString(bytes[i]));
			return hashBuilder.toString();
		} catch (NoSuchAlgorithmException e) {

			return null;
		}
	}

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

}
