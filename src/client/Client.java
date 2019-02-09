package client;

import java.io.*;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.stage.WindowEvent;
import server.Request;

public class Client extends Application implements EventHandler<ActionEvent> {
    // variable for request object
    Request req;
    //---------------------------------variables for landing and signin windows-----------------------------------------------------------------------
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
    boolean finish;
    Thread clientListner;
    
    //---------------------------------variables for landing and signin windows-----------------------------------------------------------------------
    Socket mySocket;
    ObjectOutputStream printStream;
    ObjectInputStream dataInStream;
    
    public Boolean connectToServer(){
        Boolean success;
        finish = false;
        try {
            System.out.println("step0");
            mySocket = new Socket("127.0.0.1", 7000);
            System.out.println("step-1");
            printStream = new ObjectOutputStream(mySocket.getOutputStream());
            System.out.println("step--1");
            dataInStream = new ObjectInputStream(mySocket.getInputStream());
            System.out.println("step1");
        } catch (IOException ex) {
            System.out.println("hereeeee");
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        Thread clientListner = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        System.out.println("step2");
                        Request r;
                        System.out.println("step3");
                        try {
                            r = (Request) dataInStream.readObject();
                            //System.out.println(r.getMsg()[0]+"\n");
                            System.out.println("asds\n");
                        } catch (ClassNotFoundException ex) {
                            
                            System.out.println("here1");
                            break;
                            //Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    } catch (IOException ex) {
                        try {
                            //that's when the server is closed
                            printStream.close();
                            dataInStream.close();
                            mySocket.close();
                            System.out.println("Server closed");
                        } catch (IOException ex1) {
                            System.out.println("here2");
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                    } 
                }
            }
        });
        clientListner.start();
        return true;
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        ps = primaryStage;
        if ( ! connectToServer() ){
            System.out.println("Couldn't connect to server");
            System.exit(0);
            return;
        }
        landingWinInit();
        signInWinInit();
        primaryStage.setTitle("TicTacToe");
        ps.setScene(currentScene);
        ps.show();
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
            public void handle( WindowEvent close){
                System.out.println("client closed");
                try{
                    clientListner.stop();
                    mySocket.close();
                    printStream.close();
                    dataInStream.close();
                    System.exit(0);

                }catch( Exception e){
                    System.out.println("exc");
                    System.exit(0);
                     e.printStackTrace();
                }
            }
        });

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
            System.out.println("Submit");
            String [] fields = {userameTextFld.getText(), passwordFld.getText()};
            //Request request = new Request("signin", fields , null, null);
            //sending signIn request
            req.setRequestType("signInSubmit");
            req.setUserName(userameTextFld.getText());
            req.setPassWord(passwordFld.getText());
            try {
                printStream.writeObject(req);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
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
