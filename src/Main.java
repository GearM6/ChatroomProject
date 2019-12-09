import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import javax.xml.soap.Text;

//get username
//connect to server
//get current users in server

public class Main extends Application {
    private String hostname;
    private String port;
    public static String userName = "";

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
