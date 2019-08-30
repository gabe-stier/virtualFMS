package virtualFMS;

import java.io.File;
import java.util.Comparator;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FileHandler {

	public static void listFiles(File folder, TreeItem<String> root, TreeView<String> view) {
		final File[] listOfFiles = folder.listFiles();
		File[] array;
		for (int length = (array = listOfFiles).length, i = 0; i < length; ++i) {
			final File file = array[i];
			if (file.isFile()) {
				root.getChildren().add(new TreeItem<String>(file.getName(),new ImageView(new Image(Main.class.getResourceAsStream("resources/file.png")))));
			} else {
				TreeItem<String> root2 = new TreeItem<String>(file.getName(), new ImageView(new Image(Main.class.getResourceAsStream("resources/folder.png"))));
				root.getChildren().add(root2);
				root2.setExpanded(false);
				listFiles(file, root2, view);
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
