package client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server.Player;
import server.Request;
import server.Response;

public class Client extends Application implements EventHandler<ActionEvent> {

    //-------------------------------- Player -----------------------------------------------------------------------
    Player p;
    //// Note this player will be initialized after getting response from the server that user credentials are correct

    //-------------------------------- Request Object ---------------------------------------------------------------
    Request req = new Request();
    //-------------------------------- Scenes -----------------------------------------------------------------------
    Scene landingWindowScene;
    Scene signInScene;
    Scene gameScene;
    Scene alertScene;
    Scene mainWindowScene;
    Scene currentScene;
    //------------------------------ Stage --------------------------------------------------------------------------
    Stage ps;
    //------------------------------ Thread --------------------------------------------------------------------------
    //Thread clientListner;

    //-------------------------------- Game variables ---------------------------------------------------------------
    Integer currentMove = 0;    // from 1 to 9 
    boolean isPlayerTypeX; // true for player x false for player y
    String otherPlayerName; //we may not use this (check later)
    Integer[][] newGameInitArr = {{2, 2, 2}, {2, 2, 2}, {2, 2, 2}};

    //------------------------------ Colors --------------------------------------------------------------------------    
    static Color red = Color.RED;
    static Color green = Color.CHARTREUSE;
    static Color gray = Color.LIGHTGREY;
    //------------------------------ TextFields and other variables---------------------------------------------------
    TextField userameTextFld;
    PasswordField passwordFld;
    boolean finish;
    boolean isSignIn = true;   // Flag to make difference between sign in and sign up scenes 
    //---------------------------------variables for landing and signin windows---------------------------------------
    Socket mySocket;
    ObjectOutputStream printStream;
    ObjectInputStream dataInStream;
    String errorMessage = "";

    //---------------------------------variables for main window --------------------------------------------------
    Boolean mainWinFlag = false;
    ArrayList<String> usernames = new ArrayList<>();
    ArrayList<Integer> playersStatus = new ArrayList<>();
    Map<String, Color> allPlayers = new HashMap<>();
    String[] currentPlayersData = {};
    
    //--------------------------------------variables for invitation dialogs ---------------------------------
    Alert inviteConfirm = new Alert(Alert.AlertType.CONFIRMATION);
    Alert invitationDeclined = new Alert(Alert.AlertType.INFORMATION);
    Boolean player1Cancelled = false;
    
    
    public Boolean connectToServer() {
        finish = false;
        try {
            System.out.println("step0");
            mySocket = new Socket("127.0.0.1", 5001);
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

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                while (true) {
                    try {
                        Response r;
                        try {
                            r = (Response) dataInStream.readObject();

                            handleResponse(r);
                            //System.out.println("response recieved\n");
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
                return null;
            }
        };
        task.setOnFailed(e -> {
        });

        Thread backgroundThread = new Thread(task);
        backgroundThread.start();
        return true;
    }

    public void handleResponse(Response r) {

        System.out.println(r.getReponseType());

        if (r.getReponseType().equals("signin")) {
            System.out.println("Login request received");
            if (r.getReponseStatus()) {
                System.out.println("login success");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        usernames = r.getUsers();
                        playersStatus = r.getStatus();
                        currentPlayersData = r.getCurrentPlayerData();
                        for (int i = 0; i < usernames.size(); i++) {
                            allPlayers.put(usernames.get(i), server.Server.state(playersStatus.get(i)));
                            System.out.println(usernames.get(i));
                        }
                        Request req = new Request();
                        req.setRequestType("set username");
                        req.setUserName(currentPlayersData[0]);
                        initMainWindow();
                        currentScene = mainWindowScene;
                        ps.setScene(currentScene);
                        ps.show();
                    }
                });

            } else {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        wrongCredentialsAlertInit("Wrong Login information please try again!!");
                        currentScene = alertScene;
                        ps.setScene(currentScene);

                    }
                });

                System.out.println("invalid user information");
                System.out.println(r.getMessage());
                errorMessage = r.getMessage() + " please try again";
                System.out.println(errorMessage);
            }

        } else if (r.getReponseType().equals("signup")) {
            if (r.getReponseStatus()) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        currentScene = mainWindowScene;
                        ps.setScene(currentScene);
                        ps.show();
                    }
                });
            } else {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        wrongCredentialsAlertInit("Sorry this UserName is already existed!! Please try again with different UserName");
                        currentScene = alertScene;
                        ps.setScene(currentScene);

                    }
                });

            }

        } else if (r.getReponseType().equals("invitation request")) {
            System.out.println("Iam " + currentPlayersData[0] + " I got " + r.getReponseType() + "From " + r.getUserName());
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    invitationDialog(r);
                }
            });
        } else if (r.getReponseType().equals("invitation response")) {
            System.out.println("Iam " + currentPlayersData[0] + " I got " + r.getReponseType() + "From " + r.getUserName());
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (r.getInvitationReply()) {
                        gameWinInit(newGameInitArr);
                        currentScene = gameScene;
                        ps.setScene(currentScene);
                        ps.show();
                    } else {
                        invitationDeclined.setTitle("Invitation Response");
                        invitationDeclined.setContentText(r.getUserName() + " doesn't want to play with you ياض");
//                        invitationDeclined.setContentText("Do you want to play with him?");

                        invitationDeclined.showAndWait();
                        initMainWindow();
                        currentScene = mainWindowScene;
                        ps.setScene(currentScene);
                        ps.show();
                    }
                }
            });
            
        } else if (r.getReponseType().equals("cancel invitation")) {
            System.out.println("Iam " + currentPlayersData[0] + " I got " + r.getReponseType() + "From " + r.getUserName()+"blalalalal");

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    player1Cancelled = true;
                }
            });

        } 
//        else if (r.getReponseType().equals("statuses update")) {
//            usernames = r.getUsers();
//            playersStatus = r.getStatus();
//            for (int i = 0; i < usernames.size(); i++) {
//                allPlayers = new HashMap<>();
//                allPlayers.put(usernames.get(i), server.Server.state(playersStatus.get(i)));
//            }
//            currentScene = mainWindowScene;
//            ps.setScene(currentScene);
//            ps.show();
//            System.out.println("new update");
//        }

    }

//    //--------------------------------- Start ---------------------------------------------------
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        ps = primaryStage;
        if (!connectToServer()) {
            System.out.println("Couldn't connect to server");
            System.exit(0);
            return;
        }
        landingWinInit();
        signInWinInit();
        gameWinInit(newGameInitArr);

        primaryStage.setTitle("TicTacToe");
        currentScene = landingWindowScene;
        ps.setScene(currentScene);
        ps.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent close) {
                System.out.println("client closed");
                try {
                    //clientListner.stop();
                    mySocket.close();
                    printStream.close();
                    dataInStream.close();
                    System.exit(0);

                } catch (Exception e) {
                    System.out.println("exc");
                    System.exit(0);
                    e.printStackTrace();
                }
            }
        });

    }

    //--------------------------------- Scenes Initialization ---------------------------------------------------------
    // main window
    public void initMainWindow() {
        BorderPane mainWindowPane = new BorderPane();

        //----------------------------top bar-------------------------------------------------------------------------------------//
        Button signOutButton = new Button("Sign Out");
        signOutButton.setId("signOut");
        signOutButton.setOnAction((EventHandler<ActionEvent>) this);
        signOutButton.setPrefSize(170, 30);

        HBox topBar = new HBox();
        topBar.getChildren().addAll(signOutButton);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setStyle("-fx-background-color: #000000; -fx-padding: 20px;");
        topBar.prefHeightProperty().bind(mainWindowPane.heightProperty().multiply(0.05));

        mainWindowPane.setTop(topBar);

        //----------------------------friends list--------------------------------------------------------------------------------// 
        VBox friendsListPane = new VBox();
        allPlayers.entrySet().stream().forEach((player) -> {
            if (player.getKey().equals(currentPlayersData[0])) {
                return;
            }
            Button invite = new Button("invite");
            invite.setPrefSize(90, 30);
            Button cancel = new Button("cancel");
            cancel.setPrefSize(90, 30);
            Text text = new Text(player.getKey());
            text.setFont(Font.font("Monotype Corsiva", FontWeight.BOLD, 20));
            Circle playerStatus = new Circle(5, player.getValue());

            HBox hbox = new HBox(playerStatus, text, invite, cancel);

            hbox.setSpacing(10);
            hbox.setStyle("-fx-padding: 20px;");
            friendsListPane.getChildren().add(hbox);
            cancel.setDisable(true);
            if (player.getValue() != green) {
                invite.setDisable(true);
            }
            invite.setOnAction((ActionEvent event) -> {
                cancel.setDisable(false);
                for (int i = 0; i < friendsListPane.getChildren().size(); i++) {
                    HBox temp = (HBox) friendsListPane.getChildren().get(i);
                    temp.getChildren().get(2).setDisable(true);
                }
                req = new Request();
                req.setDistUserName(player.getKey());
                req.setRequestType("invite");
                req.setUserName(currentPlayersData[0]);
                try {
                    printStream.writeObject(req);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            cancel.setOnAction((ActionEvent event) -> {
                invite.setDisable(false);
                cancel.setDisable(true);
                for (int i = 0; i < friendsListPane.getChildren().size(); i++) {
                    HBox temp = (HBox) friendsListPane.getChildren().get(i);
                    Circle cirTemp = (Circle) temp.getChildren().get(0);
                    if (cirTemp.getFill() == Color.GREEN) {
                        temp.getChildren().get(2).setDisable(false);
                    }
                }
                req = new Request();
                req.setDistUserName(player.getKey());
                req.setRequestType("cancel invitation");
                req.setUserName(currentPlayersData[0]);
                try {
                    printStream.writeObject(req);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        });

        ScrollPane friendlistPane = new ScrollPane(friendsListPane);
        friendlistPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        mainWindowPane.setLeft(friendlistPane);

        //----------------------------Score-------------------------------------------------------------------------------------//
        VBox scorePane = new VBox(10);
        Text heading = new Text("Currents Score: ");
        heading.setFont(Font.font("Monotype Corsiva", FontWeight.BOLD, 40));
        Text score = new Text(currentPlayersData[1]);
        score.setFont(Font.font("Monotype Corsiva", FontWeight.BOLD, 40));
        scorePane.getChildren().addAll(heading, score);
        scorePane.setAlignment(Pos.CENTER);

        mainWindowPane.setRight(scorePane);

        //----------------------------Score-------------------------------------------------------------------------------------//
        Button playWithMachine = new Button("Play With Machine");
        playWithMachine.setAlignment(Pos.CENTER);
        playWithMachine.setPrefSize(300, 300);
        playWithMachine.setFont(Font.font("Monotype Corsiva", FontWeight.BOLD, 40));
        playWithMachine.setWrapText(true);

        mainWindowPane.setCenter(playWithMachine);

        ScrollPane mainWindowPaneScrolled = new ScrollPane(mainWindowPane);
        mainWindowPaneScrolled.setFitToHeight(true);
        mainWindowPaneScrolled.setFitToWidth(true);
        mainWindowScene = new Scene(mainWindowPaneScrolled);
        currentScene = mainWindowScene;
    }

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
    // Game win init
    public void gameWinInit(Integer[][] x) {
        BorderPane borderPane;
        GridPane gamePane;
        FlowPane btnsPane;
        Button signOut;
        Button quitGame;
        Button newGame;
        Button[][] Buttons = new Button[3][3];
        newGame = new Button("New Game");
        newGame.setOnAction((EventHandler<ActionEvent>) this);
        newGame.setId("newGame");

        quitGame = new Button("Quit Game");
        quitGame.setOnAction((EventHandler<ActionEvent>) this);
        quitGame.setId("quitGame");

        signOut = new Button("Sign Out");
        signOut.setOnAction((EventHandler<ActionEvent>) this);
        signOut.setId("signOut");

        btnsPane = new FlowPane(Orientation.VERTICAL);
        borderPane = new BorderPane();
        gamePane = new GridPane();
        btnsPane.getChildren().addAll(newGame, quitGame, signOut);
        btnsPane.setColumnHalignment(HPos.LEFT);
        btnsPane.setAlignment(Pos.CENTER);
        btnsPane.setVgap(50);
        borderPane.setLeft(btnsPane);
        borderPane.setCenter(gamePane);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Buttons[i][j] = new Button();
                Buttons[i][j].setMinSize(100, 100);
                Buttons[i][j].setStyle("-fx-background-color:lightblue");
                Buttons[i][j].setOnAction((EventHandler<ActionEvent>) this);
                Buttons[i][j].setId("gameButton" + (i * 3 + j + 1));
                //Buttons[i][j].setText(Integer.toString(i*3+j+1));
                gamePane.add(Buttons[i][j], j, i);

                if (x[i][j] == 0) {
                    Buttons[i][j].setStyle("-fx-background-color: lightblue;-fx-font-size :4em;-fx-text-fill: red");
                    Buttons[i][j].setText("O");
                    Buttons[i][j].setDisable(true);
                } else if (x[i][j] == 1) {

                    Buttons[i][j].setStyle("-fx-background-color: lightblue;-fx-font-size :4em;-fx-text-fill: red");
                    Buttons[i][j].setText("X");
                    Buttons[i][j].setDisable(true);

                }
            }
        }
        gamePane.setHgap(5);
        gamePane.setVgap(5);
        gamePane.setAlignment(Pos.CENTER);
        gameScene = new Scene(borderPane, 500, 320);
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
        TextField errorMessageFld = new TextField(errorMessage);
        errorMessageFld.setVisible(false);//////// Make it true in case of errors

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
        SignInGridPane.add(errorMessageFld, 1, 3, 2, 1);
        signInScene = new Scene(SignInGridPane, 600, 600);
    }

    //Wrong login data alert
    public void wrongCredentialsAlertInit(String message) {

        BorderPane rootPane = new BorderPane();
        Text alertMsg = new Text(message);

        Button back = new Button("Back");
        back.setOnAction(this);
        back.setId("backToLogin");
        rootPane.setTop(alertMsg);
        rootPane.setCenter(back);
        alertScene = new Scene(rootPane, 400, 300);
    }

    //invitation window
    public void invitationDialog(Response r) {
        inviteConfirm.setTitle("Invitation Message");
        inviteConfirm.setHeaderText(r.getUserName() + " invited you to play with him");
        inviteConfirm.setContentText("Do you want to play with him?");

        ButtonType yesButton = new ButtonType("yes");
        ButtonType noButton = new ButtonType("nahh");

        inviteConfirm.getButtonTypes().setAll(yesButton, noButton);
        Optional<ButtonType> result = inviteConfirm.showAndWait();

        Request res = new Request();
        res.setRequestType("invitation response");
        res.setInvitationReply(false);
        res.setUserName(currentPlayersData[0]);
        res.setDistUserName(r.getUserName());

        if (result.get() == yesButton) {
            if(player1Cancelled){
                invitationDeclined.setTitle("Invitation Response");
                invitationDeclined.setContentText(r.getUserName() + " cancelled his invitation");
                invitationDeclined.showAndWait();
            } else {
                res.setInvitationReply(true);
                gameWinInit(newGameInitArr);

                System.out.println("Hello I accepted the invitaion!!!");
                currentScene = gameScene;
                /////////// Add line to prevent player 2 from playing any button
                ps.setScene(currentScene);
                ps.show();
                try {
                    printStream.writeObject(res);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            if(player1Cancelled){
                invitationDeclined.setTitle("Invitation Response");
                invitationDeclined.setContentText(r.getUserName() + " cancelled his invitation");
                invitationDeclined.showAndWait();
            } else {
                res.setInvitationReply(false);
                try {
                    printStream.writeObject(res);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    // --------------------------------- main -------------------------------------------------------------
    public static void main(String[] args) {
        launch(args);
    }

    //--------------------------------- Handling button clicks --------------------------------------------
    @Override
    public void handle(ActionEvent e) {
        if (currentScene == landingWindowScene) {
            if (((Control) e.getSource()).getId() == "signUpButton" || ((Control) e.getSource()).getId() == "signInButton") {
                currentScene = signInScene;
                ps.setScene(currentScene);

                if (((Control) e.getSource()).getId() == "signUpButton") {
                    isSignIn = false;
                } else {
                    isSignIn = true;
                }
            }

        } else if (currentScene == signInScene) {
            if (((Control) e.getSource()).getId() == "signInSubmitButton") {

                req = new Request();
                if (isSignIn) {
                    req.setRequestType("signInSubmit");
                } else {
                    req.setRequestType("signUpSubmit");
                }

                req.setUserName(userameTextFld.getText());
                req.setPassWord(passwordFld.getText());
                try {
                    System.out.println(req.getRequestType());

                    printStream.writeObject(req);
                    System.out.println("Sign up request sent");

                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }

                //req.setUserName(((TextField)(currentScene.lookup("#userNameField"))).getText());
                //req.setPassWord(((TextField)(currentScene.lookup("#passWordField"))).getText());
            } else if (((Control) e.getSource()).getId() == "signInClearButton") {
                //((TextField)(signInScene.lookup("#userNameField"))).setText("");
                //((TextField)(signInScene.lookup("#passWordField"))).setText("");
                userameTextFld.setText("");
                passwordFld.setText("");
            } else if (((Control) e.getSource()).getId() == "signInBackButton") {
                isSignIn = true;
                System.out.println("back is pressed");
                currentScene = landingWindowScene;
                ps.setScene(landingWindowScene);
            }
        } else if (currentScene == gameScene) {
            //req.setUserName(p.getUsername());   //  NOTE un comment this after setting current player name (after signing in correctly)
            if (((Control) e.getSource()).getId().startsWith("gameButton")) {
                //Handling game buttons
                currentMove++;
                ((Button) e.getTarget()).setStyle("-fx-background-color: lightblue;-fx-font-size :4em;-fx-text-fill: red");
                if (currentMove % 2 == 0) {
                    ((Button) e.getTarget()).setText("O");
                } else {
                    ((Button) e.getTarget()).setText("X");
                }
                ((Button) e.getTarget()).setDisable(true);
                req = new Request();
                req.setRequestType("currentMove");
                String buttonID = ((Control) e.getSource()).getId();
                req.setCurrentPlay(Character.getNumericValue(buttonID.charAt(buttonID.length() - 1)));
            } else if (((Control) e.getSource()).getId() == "newGame") {
                System.out.println("new Game is pressed");
                req.setRequestType("newGame");
            } else if (((Control) e.getSource()).getId() == "quitGame") {
                req.setRequestType("quitGame");
            } else if (((Control) e.getSource()).getId() == "signOut") {
                req.setRequestType("signOut");
            }

            try {
                printStream.writeObject(req);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(req.getRequestType());
        } else if (currentScene == alertScene) {

            currentScene = signInScene;
            ps.setScene(currentScene);

        } else if (currentScene == mainWindowScene && ((Control) e.getSource()).getId() == "signOut") {
            userameTextFld.setText("");
            passwordFld.setText("");
            currentScene = signInScene;
            currentPlayersData = null;
            ps.setScene(currentScene);
        }
    }

}
