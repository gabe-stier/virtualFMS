package virtualFMS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

public class FileVerification {
	File hashbrowns;

	// Creates the file for users.
	public static void createNewFile() {
		File hashbrowns = new File(Main.DIR + "/.hashbrowns.txt");
		try {
			hashbrowns.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		hashbrowns.setWritable(false); // These two operations ...
		hashbrowns.setReadable(false);
	}

	public static boolean fileCheck() { // Built to check and ensure that the file exists.
		File hashbrowns = new File(Main.DIR + "/.hashbrowns.txt");
		if (!hashbrowns.exists()) {
			createNewFile();
		}
		return true;

	}

	// Checks the password hash and the user between the file and input.
	public static boolean checkCredentials(String username, String pwd) { 
		ArrayList<String[]> fileUsers = null;

		try {
			fileUsers = getUsers();
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		}

		if (fileUsers != null) {
			for (String[] user : fileUsers) {
				if(user.length == 3) {
					String inputHash = PasswordManager.hash(pwd, Base64.getDecoder().decode(user[2]));
					if (username.equalsIgnoreCase(user[0]) && inputHash.equals(user[1]))
						return true;
					}
			}
			return false;
		}

		return false; }

	// Checks to see if the username is in the password file
	private static boolean checkUsername(String username, ArrayList<String[]> users) throws FileNotFoundException {
		for (String[] user : users) {
			if (user[0].equalsIgnoreCase(username))
				return true;
		}

		return false;
	}

	// public username check
	public static boolean checkUsername(String username) throws FileNotFoundException {
		return checkUsername(username, getUsers());
	}

	// gets the users and password hashes
	public static ArrayList<String[]> getUsers() throws FileNotFoundException {
		ArrayList<String[]> users = new ArrayList<String[]>();
		File hashbrowns = new File(Main.DIR + "/.hashbrowns.txt");
		hashbrowns.setReadable(true);
		Scanner sc = new Scanner(hashbrowns);
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] crud = line.split(":");
				users.add(crud);
		}
		sc.close(); // Closes the scanner
		hashbrowns.setReadable(false);
		return users;
	}

	// Creates a new user.
	public static boolean newUser(String user, String pwd, byte[] nacl) {
		String hash = PasswordManager.hash(pwd, nacl);
		File hashbrowns = new File(Main.DIR + "/.hashbrowns.txt");
		hashbrowns.setWritable(true);
		FileWriter writer;
		try {
			writer = new FileWriter(hashbrowns, true);
			writer.append(user + ":" + hash + ":" + Base64.getEncoder().encodeToString(nacl) + '\n');
			writer.close();
			hashbrowns.setWritable(false);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
