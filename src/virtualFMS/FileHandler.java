package virtualFMS;

import java.io.File;

public class FileHandler {

	public void listFiles(File folder) {
		final File[] listOfFiles = folder.listFiles();
		File[] array;
		for (int length = (array = listOfFiles).length, i = 0; i < length; ++i) {
			final File file = array[i];
			if (file.isFile()) {
				System.out.println("File - " + folder.getName() + " - " + file.getName());
			} else {
				System.out.println("Folder - " + folder.getName() + " - " + file.getName());
				listFiles(file);
			}
		}
	}

}
