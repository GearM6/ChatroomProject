import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

//get username
//connect to server
//get current users in server

public class Main extends Application {
    public String userName = "";
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //login
        this.primaryStage = new Stage();
        this.primaryStage.setTitle("Welcome to the Chat!");
        Label prompt = new Label("Enter UserName:");
        TextField userName = new TextField();
        Button login = new Button ("Login");
        login.setOnAction(event ->{
            this.userName = userName.getText();
            try {
                chatRoom();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        VBox all = new VBox(10, prompt, userName, login);
        all.setAlignment(Pos.CENTER);
        all.setPadding(new Insets(10));
        all.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                login.fire();
            }
        });


        Scene ex = new Scene(all, 300,200);
        this.primaryStage.setScene(ex);
        this.primaryStage.show();
    }

    private void chatRoom() throws IOException {
        primaryStage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatAppStyle.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Controller controller = loader.getController();

        primaryStage.setTitle("Chat Room");
        Scene scene = new Scene(root, 687, 444);

        //TextArea chatLog = (TextArea)root.lookup("#chatLog");
        ChatClient chatClient = new ChatClient("127.0.0.1", 8000, this.userName, controller);
        chatClient.execute();
        primaryStage.setScene(scene);
        this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                try {
                    chatClient.terminate();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

}
