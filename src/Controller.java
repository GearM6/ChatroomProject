import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller {
    @FXML private TextField userNameTextField;
    @FXML private Button loginButton;
    @FXML private TextField messageTextField;
    @FXML private TextArea chatLog;

    @FXML
    protected boolean attemptLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ChatAppStyle.fxml"));
        Main.userName = userNameTextField.getText();
        return true;
    }
    @FXML void sendMessage(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            System.out.println(messageTextField.getText());
        }
    }
    @FXML public void updateChatLog(String message){
        chatLog.setText(message);
    }
}
