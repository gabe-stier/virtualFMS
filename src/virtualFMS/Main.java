package virtualFMS;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	static File DIR = new File(System.getProperty("user.dir"));

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
		mainStage.setResizable(true);
		mainStage.show();

		TreeView<String> tvFilesExplore = (TreeView<String>) explore.lookup("#treeViewID");
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
				TreeItem<String> root = new TreeItem<String>(DIR.getName(),
						new ImageView(new Image(Main.class.getResourceAsStream("resources/folder.png"))));
				FileHandler.listFiles(DIR, root);
				tvFilesExplore.setRoot(root);
				root.getChildren().sort(new Comparator<TreeItem<String>>() {

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
		});

		tvFilesExplore.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {

			@Override
			public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> old_val,
					TreeItem<String> new_val) {
				TreeItem<String> selectedItem = new_val;
				if (selectedItem.getChildren().isEmpty())
					FileHandler.openFile(selectedItem, tvFilesExplore.getRoot());
				// do what ever you want
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
				String userName = txtFldUserS.getText();
				String pwdMain = pwdFldPwd1.getText();
				String pwdConfirm = pwdFldPwd2.getText();
				
				if(!pwdMain.equals(pwdConfirm)) {
					Alert matchErr = new Alert(AlertType.ERROR);
					matchErr.setContentText("Your passwords do not match.");
					matchErr.show();
				}
				boolean user = true;
				try {
					user = FileVerification.checkUsername(userName);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(user) {
					Alert matchErr = new Alert(AlertType.ERROR);
					matchErr.setContentText("Username has been taken.");
					matchErr.show();
				}

			}
		});

		/*
		 * Explorer Scene
		 */
		Button btnLogout = (Button) explore.lookup("#logoutID");
		Button btnRefresh = (Button) explore.lookup("#refreshID");

		btnLogout.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				mainStage.setScene(loginScene);
			}

		});

		btnRefresh.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				TreeItem<String> root = new TreeItem<String>(DIR.getName(),
						new ImageView(new Image(Main.class.getResourceAsStream("resources/folder.png"))));
				FileHandler.listFiles(DIR, root);
				tvFilesExplore.setRoot(root);
				root.getChildren().sort(new Comparator<TreeItem<String>>() {

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

		});

	}

}
