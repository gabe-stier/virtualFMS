package virtualFMS;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage mainStage) throws Exception {

		Parent login = FXMLLoader.load(getClass().getResource("resources/Login.fxml"));
		mainStage.setTitle("Virtual FMS");
		mainStage.setScene(new Scene(login));
		mainStage.setResizable(false);
		mainStage.show();
	}

}
