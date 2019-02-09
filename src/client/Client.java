package client;

import java.io.FileNotFoundException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
//import javafx.event.ActionEvent;
//import javafx.event.Event;
//import javafx.event.EventHandler;
import javafx.geometry.Insets;
//import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
//import javafx.scene.layout.Background;
//import javafx.scene.layout.BackgroundImage;
//import javafx.scene.layout.BackgroundPosition;
//import javafx.scene.layout.BackgroundRepeat;
//import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
//import javafx.scene.layout.StackPane;
//import javafx.scene.paint.Color;
//import static javafx.scene.paint.Color.SKYBLUE;
//import javafx.stage.Screen;
import javafx.stage.Stage;

public class Client extends Application implements EventHandler<ActionEvent> {

    Button signUpButton;
    Button signInButton;
    Scene landingWindowScene;
    Scene signInScene;
    Label usernameLabel;
    TextField userameTextFld;
    Label passwordLabel;
    PasswordField passwordFld;
    GridPane SignInGridPane;
    GridPane landingWindowGridPane;
    BorderPane signInWindowBorderPane;
    Button signInSubmitButton;
    Button signInClearButton;
    Button signInBackButton;
    HBox hbButtons;
    Scene currentScene;
    Stage ps;

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        ps = primaryStage;
        landingWinInit();
        signInWinInit();
        primaryStage.setTitle("TicTacToe");
        ps.setScene(currentScene);
        ps.show();

    }

    public void landingWinInit() {
        signUpButton = new Button("Sign Up");
        signUpButton.setOnAction((EventHandler<ActionEvent>) this);

        signInButton = new Button("Sign In");

        signInButton.setOnAction((EventHandler<ActionEvent>) this);
        landingWindowGridPane = new GridPane();
        landingWindowGridPane.setId("grid1");
        landingWindowGridPane.setAlignment(Pos.CENTER);
        landingWindowGridPane.setHgap(5);
        landingWindowGridPane.setVgap(5);
        landingWindowGridPane.setPadding(new Insets(5, 5, 5, 5));

        landingWindowGridPane.add(signUpButton, 0, 3);
        landingWindowGridPane.add(signInButton, 4, 3);
        landingWindowScene = new Scene(landingWindowGridPane, 600, 600);
        landingWindowScene.getStylesheets().add(Client.class.getResource("tictaktoe.css").toExternalForm());
        currentScene = landingWindowScene;
    }
////////////////////////////////////////////////////////////////////////////////////////////////

    public void signInWinInit() {
        usernameLabel = new Label("User Name");
        passwordLabel = new Label("Password");
        userameTextFld = new TextField();
        passwordFld = new PasswordField();
        signInSubmitButton = new Button("Submit");

        signInSubmitButton.setOnAction((EventHandler<ActionEvent>) this);
        signInClearButton = new Button("Clear");
        signInClearButton.setOnAction((EventHandler<ActionEvent>) this);
        signInBackButton = new Button("Back");
        signInBackButton.setOnAction((EventHandler<ActionEvent>) this);
        hbButtons = new HBox();
        hbButtons.setSpacing(10.0);
        hbButtons.getChildren().addAll(signInSubmitButton, signInClearButton, signInBackButton);
        signInWindowBorderPane = new BorderPane();
        ////////////////
        SignInGridPane = new GridPane();
        SignInGridPane.setAlignment(Pos.CENTER);
        SignInGridPane.setVgap(12);
        SignInGridPane.add(usernameLabel, 0, 0);
        SignInGridPane.add(userameTextFld, 1, 0);
        SignInGridPane.add(passwordLabel, 0, 1);
        SignInGridPane.add(passwordFld, 1, 1);
        SignInGridPane.add(hbButtons, 0, 2, 2, 1);
        signInScene = new Scene(SignInGridPane, 600, 600);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handle(ActionEvent e) {
        if (e.getSource() == signInSubmitButton) {
            /**
             * code here*
             */
            System.out.println("Submit");
        } else if (e.getSource() == signInBackButton) {
            ps.setScene(landingWindowScene);
        } else if (e.getSource() == signInClearButton) {
            userameTextFld.setText("");
            passwordFld.setText("");
        } else if (e.getSource() == signUpButton) {
            ps.setScene(signInScene);
        } else if (e.getSource() == signInButton) {
            ps.setScene(signInScene);
        }

    }

}
