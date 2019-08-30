package virtualFMS;

import java.io.File;
import java.io.FileWriter;

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
		}
		else {
			return true; // GABE: Check this. I wrote this and it seems inefficient.
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
}
