package virtualFMS;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage mainStage) throws Exception {

		Parent login = FXMLLoader.load(getClass().getResource("resources/Login.fxml"));
		Parent signUp = FXMLLoader.load(getClass().getResource("resources/Sign Up.fxml"));
		Parent explore = FXMLLoader.load(getClass().getResource("resources/File Explorer.fxml"));

		Scene loginScene = new Scene(login);
		Scene signUpScene = new Scene(signUp);
		Scene exploreScene = new Scene(explore);

		mainStage.setTitle("Virtual FMS");
		mainStage.setScene(loginScene);
		mainStage.setResizable(false);
		mainStage.show();

		/*
		 * Login Scene
		 */
		Button btnLogin = (Button) login.lookup("#buttonHolder").lookup("#loginID");
		Button btnSignupL = (Button) login.lookup("#buttonHolder").lookup("#signupID");
		TextField txtFldUserL = (TextField) login.lookup("#usernameID");
		PasswordField pwdFldPwdL = (PasswordField) login.lookup("#passwordID");

		btnSignupL.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				txtFldUserL.clear();
				pwdFldPwdL.clear();
				mainStage.setScene(signUpScene);
			}

		});

		btnLogin.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				txtFldUserL.clear();
				pwdFldPwdL.clear();
				mainStage.setScene(exploreScene);
			}

		});

		/*
		 * Sign up Scene
		 */
		Button btnSignupS = (Button) signUp.lookup("#buttonHolder").lookup("#signupID");
		Button btnCancel = (Button) signUp.lookup("#buttonHolder").lookup("#cancelID");
		TextField txtFldUserS = (TextField) signUp.lookup("#newUsernameID");
		PasswordField pwdFldPwd1 = (PasswordField) signUp.lookup("#password1ID");
		PasswordField pwdFldPwd2 = (PasswordField) signUp.lookup("#password2ID");

		btnCancel.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				txtFldUserS.clear();
				pwdFldPwd1.clear();
				pwdFldPwd2.clear();
				mainStage.setScene(loginScene);
			}
		});

		btnSignupS.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

			}
		});

		/*
		 * Explorer Scene
		 */
		Button btnLogout = (Button) explore.lookup("#logoutID");
		Button btnRefresh = (Button) explore.lookup("#refreshID");
		TreeView<String> tvFilesExplore = (TreeView<String>) explore.lookup("#treeViewID");

		btnLogout.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				mainStage.setScene(loginScene);
			}

		});

		btnRefresh.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

			}

		});

	}

}
