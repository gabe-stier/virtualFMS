package virtualFMS;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/*

	For hashing security.
	https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/

*/
public class PasswordManager {

	// Generates a hash for the password
	public static String hash(String pwd){
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
			e.printStackTrace();
			return null;
		}
	}

}
