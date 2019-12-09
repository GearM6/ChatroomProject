import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

public class Controller {
    @FXML private TextField userNameTextField;
    @FXML private Button loginButton;
    @FXML private TextField messageTextField;
    @FXML private TextArea chatLog;
    LinkedList<String> messageBuffer = new LinkedList<>();

    @FXML boolean listenForEnter(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            sendMessage();
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
        String audio = "send-receive.mp3";
        try{
            Media sound = new Media(new File(audio).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } catch (Exception e){
            e.printStackTrace();
        }
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
