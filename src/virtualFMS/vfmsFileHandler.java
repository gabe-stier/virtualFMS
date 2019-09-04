package virtualFMS;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;

import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class vfmsFileHandler {

	// Lists files that are in the directory. Recursive function that stops when it
	// has ran out of folder to look in.
	public static void listFiles(File folder, TreeItem<String> root) {
		final File[] listOfFiles = folder.listFiles();
		File[] array;
		for (int length = (array = listOfFiles).length, i = 0; i < length; ++i) {
			final File file = array[i];
			if (file.getName().toCharArray()[0] != '.') {
				if (file.isFile()) {
					TreeItem<String> treeFile = new TreeItem<String>(file.getName(),
							new ImageView(new Image(Main.class.getResourceAsStream("resources/file.png"))));
					root.getChildren().add(treeFile);

				} else {
					TreeItem<String> root2 = new TreeItem<String>(file.getName(),
							new ImageView(new Image(Main.class.getResourceAsStream("resources/folder.png"))));
					root.getChildren().add(root2);
					root2.setExpanded(false);
					listFiles(file, root2);
					root2.getChildren().sort(new Comparator<TreeItem<String>>() {

						@Override
						public int compare(TreeItem<String> o1, TreeItem<String> o2) {
							if (o1.getChildren().isEmpty() && o2.getChildren().isEmpty()) {
								return o1.getValue().toLowerCase().compareTo(o2.getValue().toLowerCase());
							}
							if ((o1.getChildren().isEmpty()) && !(o2.getChildren().isEmpty())) {
								return 1;
							}
							if (!(o1.getChildren().isEmpty()) && !(o2.getChildren().isEmpty())) {
								return o1.getValue().toLowerCase().compareTo(o2.getValue().toLowerCase());
							}
							return 0;
						}

					});
				}
			}
		}

	}

	// Opens files with the system default program
	public static void openFile(TreeItem<String> fileTree, TreeItem<String> root) {
		String fileLoc = getFileLoc(fileTree, "/" + fileTree.getValue(), root);

		final File file = new File(Main.DIR.getAbsolutePath() + fileLoc);
		if (Desktop.isDesktopSupported()) {
			new Thread(() -> {
				try {
					Desktop.getDesktop().open(file);
				} catch (IOException e) {
				}
			}).start();
		}

	}

	// Gets the file location to be able to open it.
	private static String getFileLoc(TreeItem<String> fileTree, String fileLoc, TreeItem<String> root) {
		TreeItem<String> parent = fileTree.getParent();
		if (!fileTree.getParent().equals(root)) {
			fileLoc = "/" + parent.getValue() + fileLoc;
			return getFileLoc(parent, fileLoc, root);
		} else {
			return fileLoc;
		}
	}

}
