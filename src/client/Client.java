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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

   Button btn1;
   Button btn2;
   Scene scene1;
   Scene scene2;
    Label laSceneUser;
    TextField tfName;
    Label laScenePass;
    PasswordField pfPwd;
    GridPane grid2;
   GridPane grid1;
   BorderPane bp;
   Button btnSubmit;
    Button btnClear;
    Button btnBack;
    HBox hbButtons;
    Scene Xscene;
   Stage ps;
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException  {
       ps=primaryStage;
       signWinInit();
       formWinInit();
       primaryStage.setTitle("TicTacToe");
        ps.setScene(Xscene);
        ps.show();
    
    }   
    public void signWinInit(){
       btn1 =new Button("Sign Up");
        btn1.setOnAction((EventHandler<ActionEvent>) this);
        
        btn2 = new Button("Sign In");
        
        btn2.setOnAction((EventHandler<ActionEvent>)this);
        grid1 = new GridPane();
        grid1.setId("grid1");
        grid1.setAlignment(Pos.CENTER);
        grid1.setHgap(5);
        grid1.setVgap(5);
        grid1.setPadding(new Insets(5,5,5,5));
        
        grid1.add(btn1,0,3);
        grid1.add(btn2,4,3);
        scene1 = new Scene(grid1,600,600);
        scene1.getStylesheets().add(Client.class.getResource("tictaktoe.css").toExternalForm());
        Xscene=scene1;
    }
////////////////////////////////////////////////////////////////////////////////////////////////
    public void formWinInit(){
            laSceneUser=new Label("User Name");
        laScenePass=new Label("Password");
        tfName = new TextField();
        pfPwd = new PasswordField();
        btnSubmit = new Button("Submit");
        
        btnSubmit.setOnAction((EventHandler<ActionEvent>)this);
        btnClear = new Button("Clear");
        btnClear.setOnAction((EventHandler<ActionEvent>) this);
        btnBack = new Button("Back");
        btnBack.setOnAction((EventHandler<ActionEvent>) this);
        hbButtons = new HBox();
        hbButtons.setSpacing(10.0);
        hbButtons.getChildren().addAll(btnSubmit, btnClear, btnBack);
        bp=new BorderPane();
       ////////////////
        grid2= new GridPane();
        grid2.setAlignment(Pos.CENTER);
        grid2.setVgap(12);
        grid2.add(laSceneUser, 0, 0);
        grid2.add(tfName, 1, 0);
        grid2.add(laScenePass, 0, 1);
        grid2.add(pfPwd, 1, 1);
        grid2.add(hbButtons, 0, 2, 2, 1);
        scene2=new Scene(grid2,600,600);
    }
    
  public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handle(ActionEvent e) {
       if(e.getSource()==btnSubmit){
           /**code here**/
           System.out.println("Submit");}
       
       else if(e.getSource()==btnBack){
            ps.setScene(scene1);
       }
       else if(e.getSource()==btnClear){ tfName.setText(""); pfPwd.setText("");}
       else if(e.getSource()==btn1){
           ps.setScene(scene2);
       }
       else if(e.getSource()==btn2){
          ps.setScene(scene2);
     }
       
        
    }

}
