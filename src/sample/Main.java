package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

//get username
//connect to server
//get current users in server

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("ChatAppStyle.fxml"));
        primaryStage.setTitle("Chat Room");
        Scene scene = new Scene(root, 687, 444);
        Node usernameTextBox = scene.lookup("#usernameTextView");
        usernameTextBox.requestFocus();
        primaryStage.setScene(scene);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
