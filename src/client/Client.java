package client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server.Request;

public class Client extends Application implements EventHandler<ActionEvent> {
    
    //-------------------------------- Player -----------------------------------------------------------------------
    Player p;
    //// Note this player will be initialized after getting response from the server that user credentials are correct
    
    //-------------------------------- Request Object ---------------------------------------------------------------
    Request req=new Request();
    //-------------------------------- Scenes -----------------------------------------------------------------------
    Scene landingWindowScene;
    Scene signInScene;
    Scene gameScene;
    Scene currentScene;
    //------------------------------ Stage --------------------------------------------------------------------------
    Stage ps;
    //------------------------------ Thread --------------------------------------------------------------------------
    Thread clientListner;
    //------------------------------ TextFields and other variables---------------------------------------------------
    TextField userameTextFld;
    PasswordField passwordFld;
    int currentMove=0;
    boolean finish;
    //---------------------------------variables for landing and signin windows---------------------------------------
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
    //--------------------------------- Start ---------------------------------------------------
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
        gameWinInit();
        
        primaryStage.setTitle("TicTacToe");
        //currentScene=gameScene;  // Un comment this to test game windows
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
    
    //--------------------------------- Scenes Initialization ---------------------------------------------------------
    // Landing window init
    public void landingWinInit() {
         Button signUpButton;
         Button signInButton;
         GridPane landingWindowGridPane;
        signUpButton = new Button("Sign Up");
        signUpButton.setId("signUpButton");
        signUpButton.setOnAction((EventHandler<ActionEvent>) this);

        signInButton = new Button("Sign In");
        signInButton.setId("signInButton");
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
        //landingWindowScene.getStylesheets().add(Client.class.getResource("tictaktoe.css").toExternalForm());
        currentScene = landingWindowScene;
    }
    // Game win init
    public void gameWinInit(){
        BorderPane borderPane;
        GridPane gamePane;
        FlowPane btnsPane;
        Button signOut;
        Button quitGame;
        Button newGame;
        Button[] Buttons=new Button[9];
        newGame=new Button("New Game");
        newGame.setOnAction((EventHandler<ActionEvent>) this);
        newGame.setId("newGame");
        
        quitGame=new Button("Quit Game");
        quitGame.setOnAction((EventHandler<ActionEvent>) this);
        quitGame.setId("quitGame");
        
        signOut=new Button("Sign Out");
        signOut.setOnAction((EventHandler<ActionEvent>) this);
        signOut.setId("signOut");
        
        btnsPane=new FlowPane(Orientation.VERTICAL);
        borderPane=new BorderPane();
        gamePane=new GridPane();
        btnsPane.getChildren().addAll(newGame,quitGame,signOut);
        btnsPane.setColumnHalignment(HPos.LEFT);
        btnsPane.setAlignment(Pos.CENTER);
        btnsPane.setVgap(50);
        borderPane.setLeft(btnsPane);
        borderPane.setCenter(gamePane);
        for(int i=0;i<9;i++){
           Buttons[i] = new Button();
           Buttons[i].setMinSize(100,100);
           Buttons[i].setStyle("-fx-background-color:lightblue");
           Buttons[i].setOnAction(this);
           Buttons[i].setId("gameButton"+(i+1));  
        }
        gamePane.add(Buttons[0],0,0);
        gamePane.add(Buttons[1],1,0);
        gamePane.add(Buttons[2],2,0);
        gamePane.add(Buttons[3],0,1);
        gamePane.add(Buttons[4],1,1);
        gamePane.add(Buttons[5],2,1);
        gamePane.add(Buttons[6],0,2);
        gamePane.add(Buttons[7],1,2);
        gamePane.add(Buttons[8],2,2);
        gamePane.setHgap(5);
        gamePane.setVgap(5);
        gamePane.setAlignment(Pos.CENTER);
        gameScene=new Scene(borderPane,500,320);
    }

    // Signin window init
    public void signInWinInit() {
        Button signInSubmitButton;
        Button signInClearButton;
        Button signInBackButton;
        GridPane SignInGridPane;
        Label usernameLabel;
        //TextField userameTextFld;
        Label passwordLabel;
        //PasswordField passwordFld;
        BorderPane signInWindowBorderPane;
        HBox hbButtons;
        usernameLabel = new Label("User Name");
        passwordLabel = new Label("Password");
        
        userameTextFld = new TextField();
        userameTextFld.setId("userNameField");
        
        passwordFld = new PasswordField();
        userameTextFld.setId("passWordField");
        
        signInSubmitButton = new Button("Submit");
        signInSubmitButton.setId("signInSubmitButton");
        signInSubmitButton.setOnAction((EventHandler<ActionEvent>) this);
        
        signInClearButton = new Button("Clear");
        signInClearButton.setId("signInClearButton");
        signInClearButton.setOnAction((EventHandler<ActionEvent>) this);
        
        signInBackButton = new Button("Back");
        signInBackButton.setId("signInBackButton");
        signInBackButton.setOnAction((EventHandler<ActionEvent>) this);
        
        hbButtons = new HBox();
        hbButtons.setSpacing(10.0);
        hbButtons.getChildren().addAll(signInSubmitButton, signInClearButton, signInBackButton);
        signInWindowBorderPane = new BorderPane();
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
    // --------------------------------- main -------------------------------------------------------------
    public static void main(String[] args) {
        launch(args);
    }

    
    //--------------------------------- Handling button clicks --------------------------------------------
    @Override
    public void handle(ActionEvent e) {
        if(currentScene==landingWindowScene){
            if (((Control)e.getSource()).getId()=="signUpButton"){
                 ps.setScene(signInScene);
            }
            else if(((Control)e.getSource()).getId()=="signInButton") {
                 ps.setScene(signInScene);
             }
                 currentScene=signInScene;   
        }
        
        else if(currentScene==signInScene){
                if (((Control)e.getSource()).getId()=="signInSubmitButton") {
                   //sending signIn request
                   req.setRequestType("signInSubmit");
                   req.setUserName(userameTextFld.getText());
                   req.setPassWord(passwordFld.getText());
                   //req.setUserName(((TextField)(currentScene.lookup("#userNameField"))).getText());
                   //req.setPassWord(((TextField)(currentScene.lookup("#passWordField"))).getText());
                try {
                   printStream.writeObject(req);
                } catch (IOException ex) {
                   Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
              }
             }  else if (((Control)e.getSource()).getId()=="signInClearButton") {
                    //((TextField)(signInScene.lookup("#userNameField"))).setText("");
                    //((TextField)(signInScene.lookup("#passWordField"))).setText("");
                    userameTextFld.setText("");
                    passwordFld.setText("");             
        } 
                else if (((Control)e.getSource()).getId()=="signUpButton"){
                    req.setRequestType("signUpSubmit");
                    req.setUserName(userameTextFld.getText());
                    req.setPassWord(passwordFld.getText());
                    //req.setUserName(((TextField)(currentScene.lookup("#userNameField"))).getText());
                    //req.setPassWord(((TextField)(currentScene.lookup("#passWordField"))).getText());
            try {
                    printStream.writeObject(req);
            } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
                else if (((Control)e.getSource()).getId()=="signInBackButton") {
                    System.out.println("back is pressed"); 
                    currentScene=landingWindowScene;
                    ps.setScene(landingWindowScene);
                }
        }
        else if(currentScene==gameScene){
            //req.setUserName(p.getUsername());   //  NOTE un comment this after setting current player name (after signing in correctly)
            if (((Control)e.getSource()).getId().startsWith("gameButton")){
                  //Handling game buttons
                    currentMove++;
                    ((Button)e.getTarget()).setStyle("-fx-background-color: lightblue;-fx-font-size :4em;-fx-text-fill: red");
                if(currentMove%2==0)
                    ((Button)e.getTarget()).setText("O");
                else 
                    ((Button)e.getTarget()).setText("X");   
                ((Button)e.getTarget()).setDisable(true);
                req.setRequestType("currentMove");
                String buttonID =((Control)e.getSource()).getId() ;
                req.setCurrentPlay(Character.getNumericValue(buttonID.charAt(buttonID.length()-1)));
            }
            else if(((Control)e.getSource()).getId()=="newGame"){
                System.out.println("new Game is pressed"); 
                req.setRequestType("newGame");
            }
            else if(((Control)e.getSource()).getId()=="quitGame"){
                req.setRequestType("quitGame");
            }
            else if(((Control)e.getSource()).getId()=="signOut"){
                req.setRequestType("signOut");
                
            }
            
            try {
                    printStream.writeObject(req);
            } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } 
           System.out.println(req.getRequestType());          
        }
    }    
    }
    
   