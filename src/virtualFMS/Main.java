package virtualFMS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		try {
			logSetUp();
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("4\n");
			launch(args);
		}
	}

	// Base directory that will be used to
	static File WDIR = new File(System.getProperty("user.dir") + "/Protected");
	static File LDIR = new File("/home/protected");
	static File DIR = null;
	static String user = "";
	final static String OS = System.getProperty("os.name").toLowerCase();
	static String currentUser = null;

	final static Logger LOGGER = Logger.getLogger("VirutalFMS");

	private static void setDir(Stage stage) {
		boolean dirExist = false;
		Alert opSys = new Alert(AlertType.INFORMATION);
		if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0) {
			if (LDIR.exists()) {
				dirExist = true;
				DIR = LDIR;
			} else
				opSys.setContentText("The required folder does not exist.");
		} else if (OS.indexOf("win") >= 0) {
			if (WDIR.exists()) {
				dirExist = true;
				DIR = WDIR;
			} else
				opSys.setContentText("The required folder does not exist.");
		} else {
			opSys.setContentText("This program does not work with your current operating system.");
		}

		if (!dirExist) {
			Optional<ButtonType> result = opSys.showAndWait();
			if (!result.isPresent())
				stage.close();
			else if (result.get() == ButtonType.OK) {
				stage.close();
			}
		}
	}

	@Override
	public void start(Stage mainStage) throws Exception {

		// Loads all the layout files
		Parent login = FXMLLoader.load(getClass().getResource("resources/Login.fxml"));
		Parent signUp = FXMLLoader.load(getClass().getResource("resources/Sign Up.fxml"));
		Parent explore = FXMLLoader.load(getClass().getResource("resources/File Explorer.fxml"));

		// Creates each of the scenes that will be used and gives the root node
		Scene loginScene = new Scene(login);
		Scene signUpScene = new Scene(signUp);
		Scene exploreScene = new Scene(explore);

		// Creates the main stage
		mainStage.setTitle("Virtual FMS");
		mainStage.setScene(loginScene);
		mainStage.setResizable(true);

		if (DIR == null) {
			setDir(mainStage);
			mainStage.show();
		}
		else
			mainStage.show();

		// Gets the node that will show the files
		TreeView<String> tvFilesExplore = (TreeView<String>) explore.lookup("#treeViewID");

		/*
		 * Login Scene
		 * 
		 * This scene is designed to get a username and a password from the user to
		 * allow the user to access the files that they could otherwise not access.
		 * 
		 */

		// Gets each node that is needed to be accessed.
		Button btnLogin = (Button) login.lookup("#buttonHolder").lookup("#loginID");
		Button btnSignupL = (Button) login.lookup("#buttonHolder").lookup("#signupID");
		TextField txtFldUserL = (TextField) login.lookup("#usernameID");
		PasswordField pwdFldPwdL = (PasswordField) login.lookup("#passwordID");

		// Sets the action that occurs when the sign up button is clicked.
		// Clears the text fields and changes the scene to the sign up scene.
		btnSignupL.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				txtFldUserL.clear();
				pwdFldPwdL.clear();
				mainStage.setScene(signUpScene);
			}

		});

		/*
		 * Sets the action that occurs when the login button is clicked. Pulls the
		 * username and password from the text fields and checks to see if they are
		 * valid. Displays an error alert if either the password or username is invalid.
		 * If the username and password are valid, it reads the files and folders in the
		 * base directory and displays then for the user to see. It will sort the
		 * results and show folders before it shows files at the same level.
		 */
		btnLogin.setOnAction(new EventHandler<ActionEvent>() {

			// TODO Check password and username to file.

			@Override
			public void handle(ActionEvent event) {
				String userName = txtFldUserL.getText();
				String pasword = pwdFldPwdL.getText();
				boolean cred = FileVerification.checkCredentials(userName, PasswordManager.hash(pasword));
				if (cred) {
					txtFldUserL.clear();
					pwdFldPwdL.clear();
					mainStage.setScene(exploreScene);
					LOGGER.info(userName + " has logged in.");
					currentUser = userName;
					TreeItem<String> root = new TreeItem<String>(DIR.getName() + "/access",
							new ImageView(new Image(Main.class.getResourceAsStream("resources/folder.png"))));
					vfmsFileHandler.listFiles(DIR, root);
					tvFilesExplore.setRoot(root);

					// Sorts the results
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
				} else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setContentText("Invalid username or password.");
					alert.show();
				}
			}
		});

		// Sets the action that occurs when the user clicks on a file.
		// Opens the default program that user has set in the operating system for the
		// file.
		tvFilesExplore.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> old_val,
					TreeItem<String> new_val) {
				TreeItem<String> selectedItem = new_val;
				if (selectedItem.getChildren().isEmpty())
					vfmsFileHandler.openFile(selectedItem, tvFilesExplore.getRoot());
			}
		});

		/*
		 * Sign up Scene
		 * 
		 * This scene is designed for the user to sign up for access to the directory
		 * 
		 */

		// All useful nodes that are in this scene.
		Button btnSignupS = (Button) signUp.lookup("#buttonHolder").lookup("#signupID");
		Button btnCancel = (Button) signUp.lookup("#buttonHolder").lookup("#cancelID");
		TextField txtFldUserS = (TextField) signUp.lookup("#newUsernameID");
		PasswordField pwdFldPwd1 = (PasswordField) signUp.lookup("#password1ID");
		PasswordField pwdFldPwd2 = (PasswordField) signUp.lookup("#password2ID");

		// Action that occurs when the user clicks the cancel button.
		// Clears the all text fields in the scene.
		btnCancel.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				txtFldUserS.clear();
				pwdFldPwd1.clear();
				pwdFldPwd2.clear();
				mainStage.setScene(loginScene);
			}
		});

		// Action that occurs when the user clicks the sign up button.
		// Obtains all the data from the text fields and checks to see if the two
		// passwords are the same and checks to see if the user name has already been
		// taken before.
		btnSignupS.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				boolean check = true;
				String userName = txtFldUserS.getText();
				String pwdMain = pwdFldPwd1.getText();
				String pwdConfirm = pwdFldPwd2.getText();

				// Checks password criteria
				if (!PasswordManager.checkPassword(pwdMain)) {
					check = false;
					Alert matchErr = new Alert(AlertType.ERROR);
					matchErr.setContentText("Password does not meet criteria.");
					matchErr.show();
				}

				// Alert if the passwords do not match
				if (!pwdMain.equals(pwdConfirm)) {
					check = false;
					Alert matchErr = new Alert(AlertType.ERROR);
					matchErr.setContentText("Passwords do not match.");
					matchErr.show();
				}
				boolean user = true;
				// Checks to see if the userfile is there
				if (FileVerification.fileCheck()) {
					try {
						user = FileVerification.checkUsername(userName); // Checks to see if the username has already
																			// been taken.
					} catch (FileNotFoundException e) {
						LOGGER.severe(e.getStackTrace().toString());
					}
				}
				// Alert if the username is taken
				if (user) {
					check = false;
					Alert usernameErr = new Alert(AlertType.ERROR);
					usernameErr.setContentText("Username has been taken.");
					usernameErr.show();
				}

				if (userName.contains(":")) {
					check = false;
					Alert usernameErr = new Alert(AlertType.ERROR);
					usernameErr.setContentText("Username can not contain a colon \":\".");
					usernameErr.show();
				}

				if (check) { // Allows access to new user if nothing is wrong with input data.
					boolean newUser = FileVerification.newUser(userName, PasswordManager.hash(pwdMain));
					if (newUser) {
						txtFldUserS.clear();
						pwdFldPwd1.clear();
						pwdFldPwd2.clear();
						mainStage.setScene(loginScene);
						LOGGER.info("A new user has been created with the name of: " + userName);
					} else {
						Alert usernameErr = new Alert(AlertType.ERROR);
						usernameErr.setContentText("An error has occured. Please try again later.");
						usernameErr.show();
					}
				}

			}
		});

		/*
		 * Explorer Scene
		 * 
		 * This scene is designed to show the files that are in the directory and allow
		 * the user to open the files.
		 * 
		 */
		Button btnLogout = (Button) explore.lookup("#logoutID");
		Button btnRefresh = (Button) explore.lookup("#refreshID");

		// Action that occurs when the button logout has been pressed.
		// Changes the scene.
		btnLogout.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				mainStage.setScene(loginScene);
				LOGGER.info(currentUser + " has logged out.");
				currentUser = null;
			}

		});

		// Sets the action of refresh button.
		// Refreshes the treeview to give the most recent view of the directory.
		btnRefresh.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				TreeItem<String> root = new TreeItem<String>(DIR.getName() + "/access",
						new ImageView(new Image(Main.class.getResourceAsStream("resources/folder.png"))));
				vfmsFileHandler.listFiles(DIR, root);
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

	@Override
	public void stop() {
		if (currentUser != null)
			LOGGER.info(currentUser + " has logged out.");
		LOGGER.info("Application has closed.");
	}

	public final static void logSetUp() throws SecurityException, IOException {
		System.out.println("2\n");
		LOGGER.setLevel(Level.INFO);
		FileHandler logFile = new FileHandler(DIR + "/virtualFMS.log");
		Logger rootLogger = Logger.getLogger("");
		Handler[] handlers = rootLogger.getHandlers();
		if (handlers[0] instanceof ConsoleHandler) {
			rootLogger.removeHandler(handlers[0]);
		}
		LOGGER.setUseParentHandlers(false);
		logFile.setFormatter(new SimpleFormatter());
		LOGGER.addHandler(logFile);
		System.out.println("3\n");
	}

}
