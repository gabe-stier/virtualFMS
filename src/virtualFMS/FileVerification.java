package virtualFMS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileVerification {
	File hashbrowns;

	public static void createNewFile() {
		File hashbrowns = new File(Main.DIR + "/.hashbrowns.txt");
		try {
			hashbrowns.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hashbrowns.setWritable(false); // These two operations ...
		hashbrowns.setReadOnly(); // ... might contradict each other. Run to check.
	}

	public static boolean fileCheck() { // Built to check and ensure that the file exists.
		File hashbrowns = new File(Main.DIR + "/.hashbrowns.txt");
		if (!hashbrowns.exists()) {
			createNewFile();
		}
		return true; // GABE: Check this. I wrote this and it seems inefficient.

		/*
		 * From what I know it is efficient enough to get the job done. But I do think
		 * that this function will only ever be called once. So you could insert the
		 * code that is in the function in to the actual use case. (AKA where you put
		 * the function call)
		 */

	}

	public static boolean checkCredentials(String username, String inputHash) { // Checking the Hash
		ArrayList<String[]> fileUsers = null;

		try {
			fileUsers = getUsers();
		} catch (FileNotFoundException e) {

		}

		if (fileUsers != null) {
			for (String[] user : fileUsers) {
				if (username.equalsIgnoreCase(user[0]) && inputHash.equals(user[1]))
					return true;
			}
			return false;
		}

		return false; // Temporary.
	}

	public String writeHash(String newPasswordHash) {
		// TODO: Need to write validation check to make sure this is hash.
		hashbrowns.setWritable(true);
		// Intending to do FileWriter to write to file on a NEW LINE.

		return null;// Temporary
	}

	// Checks to see if the username is in the password file
	public static boolean checkUsername(String username, ArrayList<String[]> users) throws FileNotFoundException {
		for (String[] user : users) {
			if (user[0].equalsIgnoreCase(username))
				return true;
		}

		return false;
	}

	public static boolean checkUsername(String username) throws FileNotFoundException {
		return checkUsername(username, getUsers());
	}

	public static ArrayList<String[]> getUsers() throws FileNotFoundException {
		ArrayList<String[]> users = new ArrayList<String[]>();
		File hashbrowns = new File(Main.DIR + "/.hashbrowns.txt");
		Scanner sc = new Scanner(hashbrowns);
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] crud = line.split(":");
			users.add(crud);
		}
		sc.close(); // Closes the scanner
		return users;
	}

	public static boolean newUser(String user, String hash) {
		File hashbrowns = new File(Main.DIR + "/.hashbrowns.txt");
		hashbrowns.setWritable(true);
		FileWriter writer;
		try {
			writer = new FileWriter(hashbrowns,true);
			writer.append(user + ":" + hash+'\n');
			writer.close();
			hashbrowns.setWritable(false);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
