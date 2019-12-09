import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//get username
//connect to server
//get current users in server

public class Main extends Application {
    private String hostname;
    private String port;
    public String userName = "";

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatAppStyle.fxml"));
        Parent root = loader.load();

        Controller controller = loader.getController();

        primaryStage.setTitle("Chat Room");
        Scene scene = new Scene(root, 687, 444);
        primaryStage.setScene(scene);
        primaryStage.show();

        //TextArea chatLog = (TextArea)root.lookup("#chatLog");
        ChatClient chatClient = new ChatClient("127.0.0.1", 8000, "userName", controller);
        chatClient.execute();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
