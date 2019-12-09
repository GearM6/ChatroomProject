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
import java.util.LinkedList;
import java.util.ResourceBundle;

public class Controller {
    @FXML private TextField userNameTextField;
    @FXML private Button loginButton;
    @FXML private TextField messageTextField;
    @FXML private TextArea chatLog;
    LinkedList<String> messageBuffer = new LinkedList<>();

    @FXML boolean listenForEnter(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            return true;
        }
        return false;
    }
    @FXML public String changeUserName(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER && userNameTextField.getText().trim().length() > 0){
            userNameTextField.setEditable(false);
            return userNameTextField.getText().trim();
        }
        else return "";
    }
    @FXML public void updateChatLog(String message){
        chatLog.appendText(message);
    }
    public String getUserName(){
        return userNameTextField.getText().trim();
    }
    @FXML
    private void sendMessage(){
        String msg = messageTextField.getText();
        messageBuffer.add(msg);
        messageTextField.setText("");
    }

    public String getNextMessage(){
        if(!messageBuffer.isEmpty()){
            return messageBuffer.remove();
        }
        else {
            return null;
        }
    }
}
