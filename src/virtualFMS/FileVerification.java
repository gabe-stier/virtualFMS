package virtualFMS;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileVerification {
	File hashbrowns;

	public void createNewFile() {
		File hashbrowns = new File("~/hashbrowns.txt");
		hashbrowns.setWritable(false); // These two operations ...
		hashbrowns.setReadOnly(); // ... might contradict each other. Run to check.
	}

	public boolean FileCheck() { // Built to check and ensure that the file exists.
		if (!hashbrowns.exists()) {
			createNewFile();
			return true;
		} else {
			return true; // GABE: Check this. I wrote this and it seems inefficient.

			/*
			 * From what I know it is efficient enough to get the job done. But I do think
			 * that this function will only ever be called once. So you could insert the
			 * code that is in the function in to the actual use case. (AKA where you put
			 * the function call)
			 */
		}
	}

	public boolean checkCredentials(String username, String inputHash) { // Checking the Hash

		return true; // Temporary.
	}

	public String writeHash(String newPasswordHash) {
		// TODO: Need to write validation check to make sure this is hash.
		hashbrowns.setWritable(true);
		// Intending to do FileWriter to write to file on a NEW LINE.

		return null;// Temporary
	}

	// Checks to see if the username is in the password file
	public static boolean checkUsername(String username) throws FileNotFoundException {
		File hashbrowns = new File("~/hashbrowns.txt");
		Scanner sc = new Scanner(hashbrowns);
		boolean found = false;
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] crud = line.split(":");
			if (username.equalsIgnoreCase(crud[0])) {
				found = true;
				break;
			}
		}
		sc.close(); // Closes the scanner
		return found; // Returns true if the username has been found.
	}
}
